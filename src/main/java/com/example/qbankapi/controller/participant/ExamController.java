package com.example.qbankapi.controller.participant;

import com.example.qbankapi.dto.model.ExamDto;
import com.example.qbankapi.dto.model.ExamSubmissionDto;
import com.example.qbankapi.dto.model.ParticipantUserAnswerDto;
import com.example.qbankapi.entity.Exam;
import com.example.qbankapi.entity.ParticipantUserExamEnrollment;
import com.example.qbankapi.entity.Question;
import com.example.qbankapi.exception.base.impl.*;
import com.example.qbankapi.service.ExamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.qbankapi.interceptor.constant.Variable.USER_ID;

@Slf4j
@Controller
@RequestMapping("/participant/exam")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @PostMapping("/{examId}/enroll")
    public String enrollExam(@PathVariable("examId") Long examId, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            Long participantId = (Long) session.getAttribute(USER_ID);

            examService.enrollExam(participantId, examId);
            log.debug("Exam enrollment successful with participant id [{}] and exam id [{}]", participantId, examId);

            redirectAttributes.addFlashAttribute("message", "You have successfully enrolled in exam.");
            redirectAttributes.addFlashAttribute("messageType", "success");

            log.info("Redirecting to /participant/exams");
            return "redirect:/participant/exams";
        } catch (ExamNotFoundException ex) {
            log.error("Exam not found with id [{}]", examId, ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Exam not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /participant/exams");
            return "redirect:/participant/exams";
        } catch (AccessDeniedException ex) {
            log.error("Access denied ex occured", ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /participant/exams");
            return "redirect:/participant/exams";
        }
    }

    @GetMapping("/start/{examEnrollmentId}")
    public String startExam(@PathVariable("examEnrollmentId") Long examEnrollmentId, HttpSession httpSession, RedirectAttributes redirectAttributes) {
        try {
            ExamDto examDto = examService.getExamInDtoByEnrollmentId(examEnrollmentId, (Long) httpSession.getAttribute(USER_ID));

            httpSession.setAttribute("examDto", examDto);
            httpSession.setAttribute("examSubmissionDto", ExamSubmissionDto.builder().examEnrollmentId(examEnrollmentId).examId(examDto.getId()).build());
            httpSession.setAttribute("currentQuestionNumber", 1);
            httpSession.setAttribute("totalQuestion", examDto.getQuestions().size());

            log.info("Redirecting to /participant/exam/question");
            return "redirect:/participant/exam/question";
        } catch (ParticipantUserExamSubmissionAlreadyExistsException ex) {
            log.info("Prevented duplicate exam submission: {}", ex.getMessage());

            redirectAttributes.addFlashAttribute("message", "You have already submitted this exam. Duplicate submissions are not allowed.");
            redirectAttributes.addFlashAttribute("messageType", "warning");

            log.info("Redirecting to /participant/calendar");
            return "redirect:/participant/calendar";
        } catch (ParticipantUserExamEnrollmentNotFoundException ex) {
            log.warn("Enrollment not found: {}", examEnrollmentId, ex);

            redirectAttributes.addFlashAttribute("message", "Invalid exam enrollment. Please try again.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /participant/calendar");
            return "redirect:/participant/calendar";
        } catch (AccessDeniedException ex) {
            log.error("Access denied for exam [{}] and user [{}]", examEnrollmentId, httpSession.getAttribute(USER_ID), ex);

            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /participant/calendar");
            return "redirect:/participant/calendar";
        }
    }

    @GetMapping("/question")
    public String getQuestion(Model model, HttpSession session, RedirectAttributes redirectAttributes) {

        Integer currentQuestionNumber = (Integer) session.getAttribute("currentQuestionNumber");
        ExamDto examDto = (ExamDto) session.getAttribute("examDto");

        if (currentQuestionNumber == null || examDto == null) {
            redirectAttributes.addFlashAttribute("message", "Session expired or invalid access.");
            redirectAttributes.addFlashAttribute("messageType", "warning");

            log.info("Redirecting to /participant/home");
            return "redirect:/participant/home";
        }

        model.addAttribute("subjectName", examDto.getSubjectName());
        model.addAttribute("currentQuestionNumber", currentQuestionNumber);
        model.addAttribute("question", examDto.getQuestions().get(currentQuestionNumber - 1));

        log.info("Rendering exam");
        return "participant/exam";
    }

    @PostMapping("/submit")
    public String processQuestion(@ModelAttribute ParticipantUserAnswerDto participantUserAnswerDto, Model model, HttpSession session, RedirectAttributes redirectAttributes) {

        Integer currentQuestionNumber = (Integer) session.getAttribute("currentQuestionNumber");
        Integer totalQuestion = (Integer) session.getAttribute("totalQuestion");
        ExamSubmissionDto examSubmissionDto = (ExamSubmissionDto) session.getAttribute("examSubmissionDto");

        if (currentQuestionNumber == null || totalQuestion == null || examSubmissionDto == null) {
            redirectAttributes.addFlashAttribute("message", "Session expired or invalid access.");
            redirectAttributes.addFlashAttribute("messageType", "warning");

            log.info("Redirecting to /participant/home");
            return "redirect:/participant/home";
        }

        examSubmissionDto.getAnswers().put(participantUserAnswerDto.getQuestionId(), participantUserAnswerDto.getAnswer());

        if (currentQuestionNumber.equals(totalQuestion)) {
            examService.saveUserExamSubmission(examSubmissionDto);

            session.removeAttribute("examDto");
            session.removeAttribute("examSubmissionDto");
            session.removeAttribute("currentQuestionNumber");
            session.removeAttribute("totalQuestion");

            redirectAttributes.addFlashAttribute("message", "Exam submitted successfully. Results will be available after the exam ends.");
            redirectAttributes.addFlashAttribute("messageType", "success");

            log.info("Redirecting to /participant/home");
            return "redirect:/participant/home";
        }

        session.setAttribute("currentQuestionNumber", currentQuestionNumber + 1);

        log.info("Redirecting to /participant/exam/question");
        return "redirect:/participant/exam/question";
    }

    @PostMapping("/exit")
    public String exitExam(HttpSession session, RedirectAttributes redirectAttributes) {
        Integer currentQuestionNumber = (Integer) session.getAttribute("currentQuestionNumber");
        Integer totalQuestion = (Integer) session.getAttribute("totalQuestion");
        ExamSubmissionDto examSubmissionDto = (ExamSubmissionDto) session.getAttribute("examSubmissionDto");
        ExamDto examDto = (ExamDto) session.getAttribute("examDto");

        if (currentQuestionNumber == null || totalQuestion == null || examSubmissionDto == null || examDto == null) {
            redirectAttributes.addFlashAttribute("message", "Session expired or invalid access.");
            redirectAttributes.addFlashAttribute("messageType", "warning");

            log.info("Redirecting to /participant/home");
            return "redirect:/participant/home";
        }

        Map<Long, Question.Option> answers = examSubmissionDto.getAnswers();
        List<ExamDto.ExamQuestionDto> questions = examDto.getQuestions();

        examService.saveUserExamSubmission(examSubmissionDto);

        Long participantId = (Long) session.getAttribute(USER_ID);
        log.info("Participant [{}] exited exam [{}]. Submission completed with unanswered questions set to null.", participantId, examDto.getId());

        while (currentQuestionNumber < totalQuestion) {
            Long questionId = questions.get(currentQuestionNumber).getId();
            answers.putIfAbsent(questionId, null);
            currentQuestionNumber++;
        }

        session.removeAttribute("examDto");
        session.removeAttribute("examSubmissionDto");
        session.removeAttribute("currentQuestionNumber");
        session.removeAttribute("totalQuestion");

        redirectAttributes.addFlashAttribute("message", "You exited the exam. Your attempted answers have been submitted. Results will be available after the exam ends.");
        redirectAttributes.addFlashAttribute("messageType", "info");

        log.info("Redirecting to /participant/home");
        return "redirect:/participant/home";
    }

}
