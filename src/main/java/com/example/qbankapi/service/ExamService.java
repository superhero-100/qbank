package com.example.qbankapi.service;

import com.example.qbankapi.dao.*;
import com.example.qbankapi.dto.model.ExamDetailsDto;
import com.example.qbankapi.dto.model.ExamFilterDto;
import com.example.qbankapi.dto.model.ExamViewPageDto;
import com.example.qbankapi.dto.request.CreateExamRequestDto;
import com.example.qbankapi.entity.*;
import com.example.qbankapi.exception.InSufficientQuestionsException;
import com.example.qbankapi.exception.SubjectNotFoundException;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamDao examDao;
    private final SubjectDao subjectDao;
    private final ExamAnalyticsDao examAnalyticsDao;
    private final QuestionDao questionDao;
//    private final UserAnalyticsDao userAnalyticsDao;
//    private final UserExamResultDao userExamResultDao;
//    private final UserAnswerDao userAnswerDao;
//    private final UserDao userDao;

    @Transactional(readOnly = true)
    public List<ExamDetailsDto> getExamInDto() {
        return examDao.findAll()
                .stream()
                .map(exam -> ExamDetailsDto.builder()
                        .id(exam.getId())
                        .description(exam.getDescription())
                        .totalMarks(exam.getTotalMarks())
                        .subjectName(exam.getSubject().getName())
                        .totalQuestions(exam.getQuestions().size())
                        .totalEnrolledUsers(exam.getEnrolledUsers().size())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void createExam(CreateExamRequestDto createExamRequestDto) {
        Subject subject = subjectDao.findById(createExamRequestDto.getSubjectId()).orElseThrow(() -> new SubjectNotFoundException("Subject Not Found"));

        ExamAnalytics examAnalytics = new ExamAnalytics();
        examAnalytics.setTotalSubmissions(0);
        examAnalytics.setAverageScore(0D);
        examAnalytics.setHighestScore(0D);
        examAnalytics.setLowestScore(0D);

        Integer totalMarks = (
                (createExamRequestDto.getTotal1MarkQuestions() * 1) +
                        (createExamRequestDto.getTotal2MarkQuestions() * 2) +
                        (createExamRequestDto.getTotal3MarkQuestions() * 3) +
                        (createExamRequestDto.getTotal4MarkQuestions() * 4) +
                        (createExamRequestDto.getTotal5MarkQuestions() * 5) +
                        (createExamRequestDto.getTotal6MarkQuestions() * 6)
        );

        Exam exam = new Exam();
        exam.setDescription(createExamRequestDto.getDescription());
        exam.setTotalMarks(totalMarks);
        exam.setSubject(subject);
        exam.setQuestions(new ArrayList<>());
        exam.setEnrolledUsers(List.of());
        exam.setAnalytics(examAnalytics);
        exam.setCreatedAt(LocalDateTime.now());
        exam.setModifiedAt(LocalDateTime.now());

        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal1MarkQuestions(), Question.Complexity.EASY, 1);
        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal2MarkQuestions(), Question.Complexity.EASY, 2);
        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal3MarkQuestions(), Question.Complexity.MEDIUM, 3);
        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal4MarkQuestions(), Question.Complexity.MEDIUM, 4);
        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal5MarkQuestions(), Question.Complexity.HARD, 5);
        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal6MarkQuestions(), Question.Complexity.HARD, 6);

        subject.getExams().add(exam);
        examAnalytics.setExam(exam);

        subjectDao.update(subject);
        examAnalyticsDao.save(examAnalytics);
        examDao.save(exam);
    }

    private void addQuestionsIfAvailable(List<Question> targetList, Long subjectId, Integer total, Question.Complexity complexity, int marks) {
        if (total == null || total <= 0) return;

        List<Question> questions = questionDao.findRandomQuestions(subjectId, total, complexity, marks);
        if (questions.size() < total) {
            throw new InSufficientQuestionsException(String.format("Not enough questions for marks: %d, complexity: %s", marks, complexity));
        }

        targetList.addAll(questions);
    }

    @Transactional(readOnly = true)
    public ExamViewPageDto getFilteredExams(ExamFilterDto examFilterDto) {
        if (examFilterDto.getSubjectId() == null || examFilterDto.getSubjectId() <= 0) examFilterDto.setSubjectId(0L);
        if (examFilterDto.getSortBy() == null || examFilterDto.getSortBy().isBlank()) examFilterDto.setSortBy("createdAt");
        if (examFilterDto.getSortOrder() == null || examFilterDto.getSortOrder().isBlank()) examFilterDto.setSortOrder("DESC");
        if (examFilterDto.getPageSize() == null || examFilterDto.getPageSize() <= 0) examFilterDto.setPageSize(10);
//        if (examFilterDto.getPageSize() != 5 && examFilterDto.getPageSize() != 10 && examFilterDto.getPageSize() != 20) examFilterDto.setPageSize(10);
        if (examFilterDto.getPage() == null || examFilterDto.getPage() < 0) examFilterDto.setPage(0);

        System.out.println("subjectId = " + Optional.ofNullable(examFilterDto.getSubjectId()));
        System.out.println("sortBy = " + Optional.ofNullable(examFilterDto.getSortBy()));
        System.out.println("sortOrder = " + Optional.ofNullable(examFilterDto.getSortOrder()));
        System.out.println("size = " + Optional.ofNullable(examFilterDto.getPageSize()));
        System.out.println("page = " + Optional.ofNullable(examFilterDto.getPage()));

        return examDao.findFilteredExams(examFilterDto);
    }

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