package com.example.qbankapi.service;

import com.example.qbankapi.dao.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamDao examDao;
    private final SubjectDao subjectDao;
    private final ExamAnalyticsDao examAnalyticsDao;
    private final QuestionDao questionDao;
    private final UserAnalyticsDao userAnalyticsDao;
    private final UserExamResultDao userExamResultDao;
    private final UserAnswerDao userAnswerDao;
    private final UserDao userDao;

//    @Transactional
//    public void createExam(CreateExamRequestDto examDTO) {
//        Subject subject = subjectDao.findById(examDTO.getSubjectId()).orElseThrow(() -> new EntityNotFoundException("Subject Not Found"));
//
//        ExamAnalytics analytics = ExamAnalytics.builder().build();
//
//        Integer totalMarks = (examDTO.getTotalEasyQuestions() * Question.Complexity.EASY.getMarks()) + (examDTO.getTotalMediumQuestions() * Question.Complexity.MEDIUM.getMarks()) + (examDTO.getTotalHardQuestions() * Question.Complexity.HARD.getMarks());
//        Exam exam = Exam.builder()
//                .description(examDTO.getDescription())
//                .totalMarks(totalMarks)
//                .subject(subject)
//                .analytics(analytics)
//                .build();
//
//        exam.getQuestions().addAll(questionDao.findRandomQuestions(subject.getId(), examDTO.getTotalEasyQuestions(), Question.Complexity.EASY));
//        exam.getQuestions().addAll(questionDao.findRandomQuestions(subject.getId(), examDTO.getTotalMediumQuestions(), Question.Complexity.MEDIUM));
//        exam.getQuestions().addAll(questionDao.findRandomQuestions(subject.getId(), examDTO.getTotalHardQuestions(), Question.Complexity.HARD));
//
//        subject.getExams().add(exam);
//        analytics.setExam(exam);
//
//        subjectDao.update(subject);
//        examAnalyticsDao.save(analytics);
//        examDao.save(exam);
//    }
//
//    @Transactional(readOnly = true)
//    public List<ExamDetailsDto> getAllInDto() {
//        return examDao.findAll()
//                .stream()
//                .map(exam -> ExamDetailsDto.builder()
//                        .id(exam.getId())
//                        .description(exam.getDescription())
//                        .totalMarks(exam.getTotalMarks())
//                        .subjectName(exam.getSubject().getName())
//                        .totalQuestions(exam.getQuestions().size())
//                        .totalEnrolledUsers(exam.getEnrolledUsers().size())
//                        .build())
//                .collect(Collectors.toList());
//    }
//
//    @Transactional(readOnly = true)
//    public ExamDto getInDtoById(Long id) {
//        Exam exam = examDao.findById(id).orElseThrow(() -> new EntityNotFoundException("Exam Not Found"));
//        return ExamDto.builder()
//                .id(exam.getId())
//                .description(exam.getDescription())
//                .totalMarks(exam.getTotalMarks())
//                .subjectName(exam.getSubject().getName())
//                .totalQuestions(exam.getQuestions().size())
//                .totalEnrolledUsers(exam.getEnrolledUsers().size())
//                .questions(exam.getQuestions()
//                        .stream()
//                        .map(question -> ExamQuestionDto.builder()
//                                .id(question.getId())
//                                .text(question.getText())
//                                .options(question.getOptions())
//                                .correctAnswer(question.getCorrectAnswer())
//                                .complexity(question.getComplexity())
//                                .build())
//                        .collect(Collectors.toList()))
//                .build();
//    }
//
//    @Transactional
//    public Long processSubmission(ExamSubmissionDto submissionDto, Long userId) {
//        User user = userDao.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));
//
//        Exam exam = examDao.findById(submissionDto.getExamId())
//                .orElseThrow(() -> new EntityNotFoundException("Exam Not Found"));
//
//        ExamAnalytics examAnalytics = exam.getAnalytics();
//
//        int totalCorrect = 0;
//        int attempted = 0;
//        List<UserAnswer> userAnswers = new ArrayList<>();
//
//        for (Map.Entry<Long, String> entry : submissionDto.getAnswers().entrySet()) {
//            Long questionId = entry.getKey();
//            String givenAnswer = entry.getValue();
//
//            Question question = questionDao.getById(questionId)
//                    .orElseThrow(() -> new EntityNotFoundException("Invalid Question ID: " + questionId));
//
//            Boolean isCorrect = question.getCorrectAnswer().equalsIgnoreCase(givenAnswer.trim());
//            if (!givenAnswer.isBlank()) attempted++;
//            if (isCorrect) totalCorrect++;
//
//            UserAnswer userAnswer = UserAnswer.builder()
//                    .question(question)
//                    .answerGiven(givenAnswer)
//                    .isCorrect(isCorrect)
//                    .build();
//
//            userAnswers.add(userAnswer);
//        }
//
//        Double accuracy = attempted > 0 ? ((double) totalCorrect / attempted) * 100.0 : 0.0;
//        UserAnalytics analytics = UserAnalytics.builder()
//                .attemptedQuestions(attempted)
//                .correctAnswers(totalCorrect)
//                .accuracy(accuracy)
//                .build();
//        userAnalyticsDao.save(analytics);
//
//        UserExamResult result = UserExamResult.builder()
//                .submittedAt(LocalDateTime.now())
//                .totalScore(totalCorrect)
//                .user(user)
//                .exam(exam)
//                .analytics(analytics)
//                .build();
//
//        result.setAnswers(userAnswers);
//        userAnswers.forEach(ans -> ans.setUserExamResult(result));
//        userExamResultDao.save(result);
//        userAnswers.stream().forEach(userAnswer -> userAnswerDao.save(userAnswer));
//
//        Integer previousCount = examAnalytics.getTotalSubmissions() != null ? examAnalytics.getTotalSubmissions() : 0;
//        Double previousAvg = examAnalytics.getAverageScore() != null ? examAnalytics.getAverageScore() : 0.0;
//
//        Integer updatedCount = previousCount + 1;
//        Double updatedAvg = ((previousAvg * previousCount) + totalCorrect) / updatedCount;
//
//        examAnalytics.setTotalSubmissions(updatedCount);
//        examAnalytics.setAverageScore(updatedAvg);
//        examAnalytics.setHighestScore(Math.max(examAnalytics.getHighestScore(), totalCorrect * 1.0));
//        examAnalytics.setLowestScore(previousCount == 0 ? totalCorrect * 1.0 : Math.min(examAnalytics.getLowestScore(), totalCorrect * 1.0));
//
//        examAnalyticsDao.update(examAnalytics);
//
//        exam.getEnrolledUsers().add(user);
//        examDao.save(exam);
//
//        user.getUserExamResults().add(result);
//        user.getEnrolledExams().add(exam);
//        userDao.update(user);
//
//        return result.getId();
//    }

}