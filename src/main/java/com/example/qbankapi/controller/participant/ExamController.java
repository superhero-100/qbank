package com.example.qbankapi.controller.participant;

import com.example.qbankapi.dto.model.ExamDto;
import com.example.qbankapi.dto.model.ExamSubmissionDto;
import com.example.qbankapi.dto.model.ParticipantUserAnswerDto;
import com.example.qbankapi.entity.ParticipantUserExamEnrollment;
import com.example.qbankapi.exception.base.impl.AccessDeniedException;
import com.example.qbankapi.exception.base.impl.ExamNotFoundException;
import com.example.qbankapi.exception.base.impl.ParticipantUserNotFoundException;
import com.example.qbankapi.service.ExamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpSession;

import java.util.Objects;

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

    //     only start if valid time as per the     cached user zone id   and if previously enrolled
    @GetMapping("/start/{examEnrollmentId}")
    public String startExam(@PathVariable("examEnrollmentId") Long examEnrollmentId, HttpSession session) {
        ExamDto examDto = examService.getExamInDtoByEnrollmentId(examEnrollmentId);

        session.setAttribute("examDto", examDto);
        session.setAttribute("examSubmissionDto", ExamSubmissionDto.builder().examId(examDto.getId()).build());
        session.setAttribute("currentQuestionNumber", 1);
        session.setAttribute("totalQuestion", examDto.getQuestions().size());

        log.info("Redirecting to /participant/exam/question");
        return "redirect:/participant/exam/question";
    }

    @GetMapping("/question")
    public String getQuestion(Model model, HttpSession session) {
        Integer currentQuestionNumber = (Integer) session.getAttribute("currentQuestionNumber");
        ExamDto examDto = (ExamDto) session.getAttribute("examDto");
        if (currentQuestionNumber == null || examDto == null) {
            return "redirect:/participant/home";
        }
        model.addAttribute("subjectName", examDto.getSubjectName());
        model.addAttribute("currentQuestionNumber", currentQuestionNumber);
        model.addAttribute("question", examDto.getQuestions().get(currentQuestionNumber - 1));
        return "participant/exam";
    }

    @PostMapping("/submit")
    public String processQuestion(@ModelAttribute ParticipantUserAnswerDto participantUserAnswerDto, Model model, HttpSession session) {
        Integer currentQuestionNumber = (Integer) session.getAttribute("currentQuestionNumber");
        Integer totalQuestion = (Integer) session.getAttribute("totalQuestion");
        ExamSubmissionDto examSubmissionDto = (ExamSubmissionDto) session.getAttribute("examSubmissionDto");
        if (currentQuestionNumber == null || totalQuestion == null || examSubmissionDto == null) {
            return "redirect:/participant/home";
        }
        examSubmissionDto.getAnswers().put(participantUserAnswerDto.getQuestionId(), participantUserAnswerDto.getAnswer());
        if (currentQuestionNumber.equals(totalQuestion)) {
            System.out.println("examSubmissionDto = " + examSubmissionDto);
//            Long userId = (Long) session.getAttribute(USER_ID);
//            Long userExamResultId = examService.processSubmission(examSubmissionDto, userId);
//            session.removeAttribute("examDto");
//            session.removeAttribute("examSubmissionDto");
//            session.removeAttribute("currentQuestionNumber");
//            session.removeAttribute("totalQuestion");
//            String path = UriComponentsBuilder.fromPath("/participant/result/exam/{id}").buildAndExpand(userExamResultId).toUriString();
//            return "redirect:" + path;
        }
        session.setAttribute("currentQuestionNumber", currentQuestionNumber + 1);
        return "redirect:/participant/exam/question";
    }

    @GetMapping("/exit")
    public String exitExam(HttpSession session) {
        session.removeAttribute("examDto");
        session.removeAttribute("examSubmissionDto");
        session.removeAttribute("currentQuestionNumber");
        session.removeAttribute("totalQuestion");
        return "redirect:/participant/home";
    }

}
