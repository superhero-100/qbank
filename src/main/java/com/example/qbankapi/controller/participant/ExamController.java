package com.example.qbankapi.controller.participant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/user/exam")
@RequiredArgsConstructor
public class ExamController {

//    private final ExamService examService;

//    @GetMapping("/enroll/{id}")
//    public String enrollExam(@PathVariable("id") Long id, HttpSession session, Model model) {
//        try {
//            examService.enrollExam((Long) session.getAttribute(USER_ID), id);
//        } catch (UserNotFoundException | ExamNotFoundException ex) {
//            model.addAttribute("message", ex.getMessage());
//            return "/user/error";
//        }
//        model.addAttribute("message", "Enrolled in exam successfully");
//        return "/user/success";
//    }
//
//    // only start if valid time as per the     cached user zone id   and if previously enrolled
//    @GetMapping("/start/{id}")
//    public String startExam(@PathVariable("id") Long id, HttpSession session) {
//        ExamDto examDto = examService.getExamInDtoById(id);
//        session.setAttribute("examDto", examDto);
//        session.setAttribute("examSubmissionDto", ExamSubmissionDto.builder().examId(examDto.getId()).build());
//        session.setAttribute("currentQuestionNumber", 1);
//        session.setAttribute("totalQuestion", examDto.getQuestions().size());
//        return "redirect:/user/exam/question";
//    }
//
//    @GetMapping("/question")
//    public String getQuestion(Model model, HttpSession session) {
//        Integer currentQuestionNumber = (Integer) session.getAttribute("currentQuestionNumber");
//        ExamDto examDto = (ExamDto) session.getAttribute("examDto");
//        if (currentQuestionNumber == null || examDto == null) {
//            return "redirect:/user/home";
//        }
//        model.addAttribute("subjectName", examDto.getSubjectName());
//        model.addAttribute("currentQuestionNumber", currentQuestionNumber);
//        model.addAttribute("question", examDto.getQuestions().get(currentQuestionNumber - 1));
//        return "/user/exam";
//    }
//
//    @PostMapping("/submit")
//    public String processQuestion(@ModelAttribute UserAnswerDto userAnswerDto, Model model, HttpSession session) {
//        Integer currentQuestionNumber = (Integer) session.getAttribute("currentQuestionNumber");
//        Integer totalQuestion = (Integer) session.getAttribute("totalQuestion");
//        ExamSubmissionDto examSubmissionDto = (ExamSubmissionDto) session.getAttribute("examSubmissionDto");
//        if (currentQuestionNumber == null || totalQuestion == null || examSubmissionDto == null) {
//            return "redirect:/user/home";
//        }
//        examSubmissionDto.getAnswers().put(userAnswerDto.getQuestionId(), userAnswerDto.getAnswer());
//        if (currentQuestionNumber.equals(totalQuestion)) {
//            Long userId = (Long) session.getAttribute(USER_ID);
//            Long userExamResultId = examService.processSubmission(examSubmissionDto, userId);
//            session.removeAttribute("examDto");
//            session.removeAttribute("examSubmissionDto");
//            session.removeAttribute("currentQuestionNumber");
//            session.removeAttribute("totalQuestion");
//            String path = UriComponentsBuilder.fromPath("/user/result/exam/{id}").buildAndExpand(userExamResultId).toUriString();
//            return "redirect:" + path;
//        }
//        session.setAttribute("currentQuestionNumber", currentQuestionNumber + 1);
//        return "redirect:/user/exam/question";
//    }
//
//    @GetMapping("/exit")
//    public String exitExam(HttpSession session) {
//        session.removeAttribute("examDto");
//        session.removeAttribute("examSubmissionDto");
//        session.removeAttribute("currentQuestionNumber");
//        session.removeAttribute("totalQuestion");
//        return "redirect:/user/home";
//    }

}
