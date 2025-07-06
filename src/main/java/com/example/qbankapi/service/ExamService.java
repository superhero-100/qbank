package com.example.qbankapi.service;

import com.example.qbankapi.dao.*;
import com.example.qbankapi.dto.model.ExamDto;
import com.example.qbankapi.dto.model.ExamFilterDto;
import com.example.qbankapi.dto.model.ExamSubmissionDto;
import com.example.qbankapi.dto.request.CreateExamRequestDto;
import com.example.qbankapi.dto.view.ExamPageViewDto;
import com.example.qbankapi.dto.view.ExamViewDto;
import com.example.qbankapi.entity.*;
import com.example.qbankapi.exception.base.impl.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamDao examDao;
    private final AdminUserDao adminUserDao;
    private final SubjectDao subjectDao;
    private final QuestionDao questionDao;
    private final ExamAnalyticsDao examAnalyticsDao;
    private final ParticipantUserDao participantUserDao;
    private final UserAnalyticsDao userAnalyticsDao;
    private final UserExamResultDao userExamResultDao;
    private final UserAnswerDao userAnswerDao;

    @Transactional(readOnly = true)
    public ExamPageViewDto getFilteredExams(ExamFilterDto examFilterDto) {
        log.info("Invoked getFilteredExams with initial filter: {}", examFilterDto);

        if (examFilterDto.getSubjectId() == null || examFilterDto.getSubjectId() <= 0) {
            log.debug("Invalid or missing subjectId. Defaulting to 0");
            examFilterDto.setSubjectId(0L);
        }

        if (examFilterDto.getSortBy() == null || examFilterDto.getSortBy().isBlank()) {
            log.debug("Missing sortBy. Defaulting to 'createdAt'");
            examFilterDto.setSortBy("createdAt");
        }

        if (examFilterDto.getSortOrder() == null || examFilterDto.getSortOrder().isBlank()) {
            log.debug("Missing sortOrder. Defaulting to 'DESC'");
            examFilterDto.setSortOrder("DESC");
        }

        if (examFilterDto.getPageSize() == null || examFilterDto.getPageSize() <= 0) {
            log.debug("Missing or invalid pageSize. Defaulting to 10");
            examFilterDto.setPageSize(10);
        }

        if (examFilterDto.getPageSize() != 5 && examFilterDto.getPageSize() != 10 && examFilterDto.getPageSize() != 20) {
            log.warn("Unsupported pageSize: {}. Resetting to default (10)", examFilterDto.getPageSize());
            examFilterDto.setPageSize(10);
        }

        if (examFilterDto.getPage() == null || examFilterDto.getPage() < 0) {
            log.debug("Missing or invalid page number. Defaulting to 0");
            examFilterDto.setPage(0);
        }

        log.info("Final applied filters - subjectId: {}, sortBy: {}, sortOrder: {}, pageSize: {}, page: {}", examFilterDto.getSubjectId(), examFilterDto.getSortBy(), examFilterDto.getSortOrder(), examFilterDto.getPageSize(), examFilterDto.getPage());

        ExamPageViewDto examPageViewDto = examDao.findFilteredExams(examFilterDto);

        log.info("Retrieved {} exams with applied filters", examPageViewDto.getExams().size());

        return examPageViewDto;
    }

    @Transactional
    public void createExam(CreateExamRequestDto createExamRequestDto, Long adminId) {
        log.info("Starting exam creation for adminId: {}", adminId);

        AdminUser admin = adminUserDao.findById(adminId).orElseThrow(() -> new AdminUserNotFoundException(String.format("Admin not found with id", adminId)));
        log.debug("Admin found: {}", admin.getId());

        Subject subject = subjectDao.findById(createExamRequestDto.getSubjectId()).orElseThrow(() -> new SubjectNotFoundException(String.format("Subject not found with id", createExamRequestDto.getSubjectId())));
        log.debug("Subject found: {}", subject.getId());

        ExamAnalytics examAnalytics = new ExamAnalytics();
        examAnalytics.setTotalSubmissions(0);
        examAnalytics.setAverageScore(0D);
        examAnalytics.setHighestScore(0D);
        examAnalytics.setLowestScore(0D);

        Integer totalMarks = ((createExamRequestDto.getTotal1MarkQuestions()) + (createExamRequestDto.getTotal2MarkQuestions() * 2) + (createExamRequestDto.getTotal3MarkQuestions() * 3) + (createExamRequestDto.getTotal4MarkQuestions() * 4) + (createExamRequestDto.getTotal5MarkQuestions() * 5) + (createExamRequestDto.getTotal6MarkQuestions() * 6));
        log.info("Total calculated marks: {}", totalMarks);

        ZoneId userZoneId = ZoneId.of(admin.getZoneId());

        Exam exam = new Exam();
        exam.setDescription(createExamRequestDto.getDescription());
        exam.setTotalMarks(totalMarks);
        exam.setSubject(subject);
        exam.setQuestions(new ArrayList<>());
        exam.setEnrolledParticipantUsers(List.of());
        exam.setCompletedParticipantUsers(List.of());
        exam.setAnalytics(examAnalytics);
        exam.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC));
        exam.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
        exam.setEnrollmentStartDate(createExamRequestDto.getEnrollmentStartDate().atZone(userZoneId).withZoneSameInstant(ZoneOffset.UTC));
        exam.setEnrollmentEndDate(createExamRequestDto.getEnrollmentEndDate().atZone(userZoneId).withZoneSameInstant(ZoneOffset.UTC));
        exam.setExamStartDate(createExamRequestDto.getExamStartDate().atZone(userZoneId).withZoneSameInstant(ZoneOffset.UTC));
        exam.setExamEndDate(createExamRequestDto.getExamEndDate().atZone(userZoneId).withZoneSameInstant(ZoneOffset.UTC));

        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal1MarkQuestions(), Question.Complexity.EASY, 1);
        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal2MarkQuestions(), Question.Complexity.EASY, 2);
        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal3MarkQuestions(), Question.Complexity.MEDIUM, 3);
        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal4MarkQuestions(), Question.Complexity.MEDIUM, 4);
        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal5MarkQuestions(), Question.Complexity.HARD, 5);
        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal6MarkQuestions(), Question.Complexity.HARD, 6);

        subject.getExams().add(exam);
        examAnalytics.setExam(exam);

        admin.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));

        adminUserDao.update(admin);
        subjectDao.update(subject);
        examAnalyticsDao.save(examAnalytics);
        examDao.save(exam);
    }

    private void addQuestionsIfAvailable(List<Question> targetList, Long subjectId, Integer total, Question.Complexity complexity, int marks) {
        if (total == null || total <= 0) return;

        List<Question> questions = questionDao.findRandomQuestions(subjectId, total, complexity, marks);

        if (questions.size() < total) {
            throw new InsufficientQuestionsException(String.format("Not enough questions for marks: %d, complexity: %s", marks, complexity));
        }

        targetList.addAll(questions);
    }

    @Transactional(readOnly = true)
    public List<ExamViewDto> getAllExamsInDto(Long userId) {
//        ParticipantUser participantUser = participantUserDao.findById(userId).orElseThrow(() -> new ParticipantUserNotFoundException(String.format("User not found with id: %d", userId)));
//        find exams by the start date end date and by participant user           process query's
//        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneId.of(participantUser.getZoneId())).withZoneSameInstant(ZoneOffset.UTC);
//        return examDao.findAllByEnrollmentEndDate(nowUtc, user)
        return examDao.findAllExams().stream().map(exam -> ExamViewDto.builder().id(exam.getId()).description(exam.getDescription()).totalMarks(exam.getTotalMarks()).subjectName(exam.getSubject().getName()).totalQuestions(exam.getQuestions().size()).totalEnrolledUsers(exam.getEnrolledParticipantUsers().size()).build()).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ExamViewDto> getExamsInDtoBySubjectId(Long subjectId, Long userId) {
//        ParticipantUser participantUser = participantUserDao.findById(userId).orElseThrow(() -> new ParticipantUserNotFoundException(String.format("User not found with id: %d", userId)));
//        find exams by the start date end date and by participant user           process query's
//        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneId.of(participantUser.getZoneId())).withZoneSameInstant(ZoneOffset.UTC);
//        return examDao.findAllByEnrollmentEndDateAndSubjectId(nowUtc, subjectId, user)
        return examDao.findAllExamsBySubjectId(subjectId).stream().map(exam -> ExamViewDto.builder().id(exam.getId()).description(exam.getDescription()).totalMarks(exam.getTotalMarks()).subjectName(exam.getSubject().getName()).totalQuestions(exam.getQuestions().size()).totalEnrolledUsers(exam.getEnrolledParticipantUsers().size()).build()).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ExamDto getExamInDtoById(Long id) {
        Exam exam = examDao.findById(id).orElseThrow(() -> new ExamNotFoundException(String.format("Exam Not Found with id: %d", id)));
        return ExamDto.builder()
                .id(exam.getId())
                .description(exam.getDescription())
                .totalMarks(exam.getTotalMarks())
                .subjectName(exam.getSubject().getName())
                .totalQuestions(exam.getQuestions().size())
                .totalEnrolledUsers(exam.getEnrolledParticipantUsers().size())
                .questions(exam.getQuestions()
                        .stream()
                        .map(question -> ExamDto.ExamQuestionDto.builder()
                                .id(question.getId())
                                .text(question.getText())
                                .options(question.getOptions())
                                .correctAnswer(question.getCorrectAnswer())
                                .complexity(question.getComplexity())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public Long processSubmission(ExamSubmissionDto submissionDto, Long userId) {
        ParticipantUser participantUser = participantUserDao.findById(userId)
                .orElseThrow(() -> new ParticipantUserNotFoundException(String.format("User not found with id: %d", userId)));

        Exam exam = examDao.findById(submissionDto.getExamId())
                .orElseThrow(() -> new ExamNotFoundException(String.format("Exam not found with id: %d", submissionDto.getExamId())));

        ExamAnalytics examAnalytics = exam.getAnalytics();

        int totalCorrect = 0;
        int attempted = 0;
        List<UserAnswer> userAnswers = new ArrayList<>();

        for (Map.Entry<Long, Question.Option> entry : submissionDto.getAnswers().entrySet()) {
            Long questionId = entry.getKey();
            Optional<Question.Option> givenNullableAnswer = Optional.ofNullable(entry.getValue());

            Question question = questionDao.findById(questionId)
                    .orElseThrow(() -> new QuestionNotFoundException("Invalid Question ID: " + questionId));

            boolean isCorrect = false;
            if (givenNullableAnswer.isPresent()) {
                isCorrect = givenNullableAnswer.get().equals(question.getCorrectAnswer());
            }

            if (givenNullableAnswer.isPresent()) attempted++;
            if (isCorrect) totalCorrect++;

            UserAnswer userAnswer = new UserAnswer();
            userAnswer.setQuestion(question);
            userAnswer.setAnswerGiven(givenNullableAnswer.orElse(null));
            userAnswer.setIsCorrect(isCorrect);
            userAnswers.add(userAnswer);
        }

        Double accuracy = attempted > 0 ? ((double) totalCorrect / attempted) * 100.0 : 0.0;
        UserAnalytics userAnalytics = new UserAnalytics();
        userAnalytics.setAttemptedQuestions(attempted);
        userAnalytics.setCorrectAnswers(totalCorrect);
        userAnalytics.setAccuracy(accuracy);
        userAnalyticsDao.save(userAnalytics);

        UserExamResult userExamResult = new UserExamResult();
        //local date used for submit exam
        userExamResult.setSubmittedAt(LocalDateTime.now());
        userExamResult.setTotalScore(totalCorrect);
        userExamResult.setParticipantUser(participantUser);
        userExamResult.setExam(exam);
        userExamResult.setAnalytics(userAnalytics);
        userExamResult.setAnswers(userAnswers);

        userAnswers.forEach(ans -> ans.setUserExamResult(userExamResult));
        userExamResultDao.save(userExamResult);

        userAnalytics.setUserExamResult(userExamResult);
        userAnalyticsDao.update(userAnalytics);

        userAnswers.forEach(userAnswer -> {
            userAnswer.setUserExamResult(userExamResult);
            userAnswerDao.save(userAnswer);
        });

        int previousCount = examAnalytics.getTotalSubmissions() != null ? examAnalytics.getTotalSubmissions() : 0;
        double previousAvg = examAnalytics.getAverageScore() != null ? examAnalytics.getAverageScore() : 0.0;

        int updatedCount = previousCount + 1;
        Double updatedAvg = ((previousAvg * previousCount) + totalCorrect) / updatedCount;

        examAnalytics.setTotalSubmissions(updatedCount);
        examAnalytics.setAverageScore(updatedAvg);
        examAnalytics.setHighestScore(Math.max(examAnalytics.getHighestScore(), totalCorrect * 1.0));
        examAnalytics.setLowestScore(previousCount == 0 ? totalCorrect * 1.0 : Math.min(examAnalytics.getLowestScore(), totalCorrect * 1.0));

        examAnalyticsDao.update(examAnalytics);

        exam.getEnrolledParticipantUsers().add(participantUser);
        //      add in completed participant users

        examDao.save(exam);

        participantUser.getUserExamResults().add(userExamResult);
        participantUser.getEnrolledExams().add(exam);
        participantUser.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
        participantUserDao.update(participantUser);

        return userExamResult.getId();
    }

//    @Transactional
//    public void enrollExam(Long userId, Long examId) {
//        User user = userDao.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format("User not found with id: %d", userId)));
//        Exam exam = examDao.findById(examId).orElseThrow(() -> new ExamNotFoundException(String.format("Exam not found with id: %d", examId)));
//        user.getEnrolledExams().add(exam);
//        exam.getEnrolledUsers().add(user);
//        user.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
//        exam.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
//        userDao.update(user);
//        examDao.update(exam);
//    }

}