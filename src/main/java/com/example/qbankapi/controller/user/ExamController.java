package com.example.qbankapi.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user/exam")
@RequiredArgsConstructor
public class ExamController {

//    private final ExamService examService;

//    @GetMapping("/start/{id}")
//    public String startExam(@PathVariable("id") Long id, HttpSession session) {
//        ExamDto examDto = examService.getInDtoById(id);
//        session.setAttribute("examDto", examDto);
//        session.setAttribute("examSubmissionDto", ExamSubmissionDto.builder().examId(examDto.getId()).build());
//        session.setAttribute("currentQuestionNumber", 1);
//        session.setAttribute("totalQuestion", examDto.getQuestions().size());
//        return "redirect:/exam/question";
//    }
//
//    @GetMapping("/question")
//    public String getQuestion(Model model, HttpSession session) {
//        Integer currentQuestionNumber = (Integer) session.getAttribute("currentQuestionNumber");
//        ExamDto examDto = (ExamDto) session.getAttribute("examDto");
//        if (currentQuestionNumber == null || examDto == null) {
//            return "redirect:/home";
//        }
//        model.addAttribute("subjectName", examDto.getSubjectName());
//        model.addAttribute("currentQuestionNumber", currentQuestionNumber);
//        model.addAttribute("question", examDto.getQuestions().get(currentQuestionNumber - 1));
//        return "exam";
//    }
//
//    @PostMapping("/submit")
//    public String processQuestion(@ModelAttribute UserAnswerDto userAnswerDto, Model model, HttpSession session) {
//        Integer currentQuestionNumber = (Integer) session.getAttribute("currentQuestionNumber");
//        Integer totalQuestion = (Integer) session.getAttribute("totalQuestion");
//        ExamSubmissionDto examSubmissionDto = (ExamSubmissionDto) session.getAttribute("examSubmissionDto");
//        if (currentQuestionNumber == null || totalQuestion == null || examSubmissionDto == null) {
//            return "redirect:/home";
//        }
//        examSubmissionDto.getAnswers().put(userAnswerDto.getQuestionId(), Optional.ofNullable(userAnswerDto.getAnswer()).orElse(""));
//        if (currentQuestionNumber == totalQuestion) {
//            Long userId = (Long) session.getAttribute(SessionValidationInterceptor.USER_ID);
//            Long userExamResultId = examService.processSubmission(examSubmissionDto, userId);
//            session.removeAttribute("examDto");
//            session.removeAttribute("examSubmissionDto");
//            session.removeAttribute("currentQuestionNumber");
//            session.removeAttribute("totalQuestion");
//            String path = UriComponentsBuilder.fromPath("/result/exam/{id}").buildAndExpand(userExamResultId).toUriString();
//            return "redirect:" + path;
//        }
//        session.setAttribute("currentQuestionNumber", currentQuestionNumber + 1);
//        return "redirect:/exam/question";
//    }
//
//    @GetMapping("/exit")
//    public String exitExam(HttpSession session) {
//        session.removeAttribute("examDto");
//        session.removeAttribute("examSubmissionDto");
//        session.removeAttribute("currentQuestionNumber");
//        session.removeAttribute("totalQuestion");
//        return "redirect:/home";
//    }

}
