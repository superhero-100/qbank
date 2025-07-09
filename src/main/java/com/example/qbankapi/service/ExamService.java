package com.example.qbankapi.service;

import com.example.qbankapi.dao.*;
import com.example.qbankapi.dto.model.ExamFilterDto;
import com.example.qbankapi.dto.request.CreateExamRequestDto;
import com.example.qbankapi.dto.view.ExamPageViewDto;
import com.example.qbankapi.entity.*;
import com.example.qbankapi.exception.base.impl.AdminUserNotFoundException;
import com.example.qbankapi.exception.base.impl.InsufficientQuestionsException;
import com.example.qbankapi.exception.base.impl.SubjectNotFoundException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamDao examDao;
    private final AdminUserDao adminUserDao;
    private final SubjectDao subjectDao;
    private final QuestionDao questionDao;
    private final ExamAnalyticsDao examAnalyticsDao;
//    private final ParticipantUserDao participantUserDao;
//    private final UserAnalyticsDao userAnalyticsDao;
//    private final UserExamResultDao userExamResultDao;
//    private final UserAnswerDao userAnswerDao;

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
        examAnalytics.setEnrollmentsCount(0);
        examAnalytics.setSubmissionsCount(0);
        examAnalytics.setAverageScore(0D);
        examAnalytics.setHighestScore(0D);
        examAnalytics.setLowestScore(0D);

        examAnalyticsDao.save(examAnalytics);

        Integer totalMarks = (createExamRequestDto.getTotal1MarkQuestions() + (createExamRequestDto.getTotal2MarkQuestions() * 2) + (createExamRequestDto.getTotal3MarkQuestions() * 3) + (createExamRequestDto.getTotal4MarkQuestions() * 4) + (createExamRequestDto.getTotal5MarkQuestions() * 5) + (createExamRequestDto.getTotal6MarkQuestions() * 6));
        log.info("Total calculated marks: {}", totalMarks);

        ZoneId userZoneId = ZoneId.of(admin.getZoneId());

        Exam exam = new Exam();
        exam.setDescription(createExamRequestDto.getDescription());
        exam.setTotalMarks(totalMarks);
        exam.setSubject(subject);
        exam.setQuestions(new ArrayList<>());
        exam.setParticipantEnrollments(List.of());
        exam.setParticipantUserExamSubmissions(List.of());
        exam.setParticipantUserExamResults(List.of());
        exam.setExamAnalytics(examAnalytics);
        exam.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC));
        exam.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
        exam.setEnrollmentStartDate(createExamRequestDto.getEnrollmentStartDate().atZone(userZoneId).withZoneSameInstant(ZoneOffset.UTC));
        exam.setEnrollmentEndDate(createExamRequestDto.getEnrollmentEndDate().atZone(userZoneId).withZoneSameInstant(ZoneOffset.UTC));
        exam.setExamStartDate(createExamRequestDto.getExamStartDate().atZone(userZoneId).withZoneSameInstant(ZoneOffset.UTC));
        exam.setExamEndDate(createExamRequestDto.getExamEndDate().atZone(userZoneId).withZoneSameInstant(ZoneOffset.UTC));
        exam.setCreatedByBaseUser(admin);

        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal1MarkQuestions(), Question.Complexity.EASY, 1);
        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal2MarkQuestions(), Question.Complexity.EASY, 2);
        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal3MarkQuestions(), Question.Complexity.MEDIUM, 3);
        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal4MarkQuestions(), Question.Complexity.MEDIUM, 4);
        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal5MarkQuestions(), Question.Complexity.HARD, 5);
        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal6MarkQuestions(), Question.Complexity.HARD, 6);

        examDao.save(exam);
    }

    private void addQuestionsIfAvailable(List<Question> targetList, Long subjectId, Integer total, Question.Complexity complexity, int marks) {
        if (total <= 0) return;

        List<Question> questions = questionDao.findRandomQuestions(subjectId, total, complexity, marks);
        log.debug("Random questions found questionsCount: {}", questions.size());

        if (questions.size() < total) {
            throw new InsufficientQuestionsException(String.format("Not enough questions for marks: %d, complexity: %s", marks, complexity));
        }

        targetList.addAll(questions);
        log.info("Added {} questions to target list for subjectId: {}, complexity: {}, marks: {}", questions.size(), subjectId, complexity, marks);
    }

//    @Transactional(readOnly = true)
//    public List<ExamViewDto> getAllExamsInDto(Long userId) {
////        ParticipantUser participantUser = participantUserDao.findById(userId).orElseThrow(() -> new ParticipantUserNotFoundException(String.format("User not found with id: %d", userId)));
////        find exams by the start date end date and by participant user           process query's
////        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneId.of(participantUser.getZoneId())).withZoneSameInstant(ZoneOffset.UTC);
////        return examDao.findAllByEnrollmentEndDate(nowUtc, user)
//        return examDao.findAllExams().stream().map(exam -> ExamViewDto.builder().id(exam.getId()).description(exam.getDescription()).totalMarks(exam.getTotalMarks()).subjectName(exam.getSubject().getName()).totalQuestions(exam.getQuestions().size()).totalEnrolledUsers(exam.getEnrolledParticipantUsers().size()).build()).collect(Collectors.toList());
//    }
//
//    @Transactional(readOnly = true)
//    public List<ExamViewDto> getExamsInDtoBySubjectId(Long subjectId, Long userId) {
////        ParticipantUser participantUser = participantUserDao.findById(userId).orElseThrow(() -> new ParticipantUserNotFoundException(String.format("User not found with id: %d", userId)));
////        find exams by the start date end date and by participant user           process query's
////        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneId.of(participantUser.getZoneId())).withZoneSameInstant(ZoneOffset.UTC);
////        return examDao.findAllByEnrollmentEndDateAndSubjectId(nowUtc, subjectId, user)
//        return examDao.findAllExamsBySubjectId(subjectId).stream().map(exam -> ExamViewDto.builder().id(exam.getId()).description(exam.getDescription()).totalMarks(exam.getTotalMarks()).subjectName(exam.getSubject().getName()).totalQuestions(exam.getQuestions().size()).totalEnrolledUsers(exam.getEnrolledParticipantUsers().size()).build()).collect(Collectors.toList());
//    }
//
//    @Transactional(readOnly = true)
//    public ExamDto getExamInDtoById(Long id) {
//        Exam exam = examDao.findById(id).orElseThrow(() -> new ExamNotFoundException(String.format("Exam Not Found with id: %d", id)));
//        return ExamDto.builder()
//                .id(exam.getId())
//                .description(exam.getDescription())
//                .totalMarks(exam.getTotalMarks())
//                .subjectName(exam.getSubject().getName())
//                .totalQuestions(exam.getQuestions().size())
//                .totalEnrolledUsers(exam.getEnrolledParticipantUsers().size())
//                .questions(exam.getQuestions()
//                        .stream()
//                        .map(question -> ExamDto.ExamQuestionDto.builder()
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
//        ParticipantUser participantUser = participantUserDao.findById(userId)
//                .orElseThrow(() -> new ParticipantUserNotFoundException(String.format("User not found with id: %d", userId)));
//
//        Exam exam = examDao.findById(submissionDto.getExamId())
//                .orElseThrow(() -> new ExamNotFoundException(String.format("Exam not found with id: %d", submissionDto.getExamId())));
//
//        ExamAnalytics examAnalytics = exam.getAnalytics();
//
//        int totalCorrect = 0;
//        int attempted = 0;
//        List<ParticipantUserExamQuestionAnswer> participantUserExamQuestionAnswers = new ArrayList<>();
//
//        for (Map.Entry<Long, Question.Option> entry : submissionDto.getAnswers().entrySet()) {
//            Long questionId = entry.getKey();
//            Optional<Question.Option> givenNullableAnswer = Optional.ofNullable(entry.getValue());
//
//            Question question = questionDao.findById(questionId)
//                    .orElseThrow(() -> new QuestionNotFoundException("Invalid Question ID: " + questionId));
//
//            boolean isCorrect = false;
//            if (givenNullableAnswer.isPresent()) {
//                isCorrect = givenNullableAnswer.get().equals(question.getCorrectAnswer());
//            }
//
//            if (givenNullableAnswer.isPresent()) attempted++;
//            if (isCorrect) totalCorrect++;
//
//            ParticipantUserExamQuestionAnswer participantUserExamQuestionAnswer = new ParticipantUserExamQuestionAnswer();
//            participantUserExamQuestionAnswer.setQuestion(question);
//            participantUserExamQuestionAnswer.setAnswerGiven(givenNullableAnswer.orElse(null));
//            participantUserExamQuestionAnswer.setIsCorrect(isCorrect);
//            participantUserExamQuestionAnswers.add(participantUserExamQuestionAnswer);
//        }
//
//        Double accuracy = attempted > 0 ? ((double) totalCorrect / attempted) * 100.0 : 0.0;
//        ParticipantUserExamAnalytics participantUserExamAnalytics = new ParticipantUserExamAnalytics();
//        participantUserExamAnalytics.setAttemptedQuestions(attempted);
//        participantUserExamAnalytics.setCorrectAnswers(totalCorrect);
//        participantUserExamAnalytics.setAccuracy(accuracy);
//        userAnalyticsDao.save(participantUserExamAnalytics);
//
//        ParticipantUserExamResult participantUserExamResult = new ParticipantUserExamResult();
//        //local date used for submit exam
//        participantUserExamResult.setSubmittedAt(LocalDateTime.now());
//        participantUserExamResult.setTotalScore(totalCorrect);
//        participantUserExamResult.setParticipantUser(participantUser);
//        participantUserExamResult.setExam(exam);
//        participantUserExamResult.setAnalytics(participantUserExamAnalytics);
//        participantUserExamResult.setAnswers(participantUserExamQuestionAnswers);
//
//        participantUserExamQuestionAnswers.forEach(ans -> ans.setParticipantUserExamResult(participantUserExamResult));
//        userExamResultDao.save(participantUserExamResult);
//
//        participantUserExamAnalytics.setParticipantUserExamResult(participantUserExamResult);
//        userAnalyticsDao.update(participantUserExamAnalytics);
//
//        participantUserExamQuestionAnswers.forEach(participantUserExamQuestionAnswer -> {
//            participantUserExamQuestionAnswer.setParticipantUserExamResult(participantUserExamResult);
//            userAnswerDao.save(participantUserExamQuestionAnswer);
//        });
//
//        int previousCount = examAnalytics.getTotalSubmissions() != null ? examAnalytics.getTotalSubmissions() : 0;
//        double previousAvg = examAnalytics.getAverageScore() != null ? examAnalytics.getAverageScore() : 0.0;
//
//        int updatedCount = previousCount + 1;
//        Double updatedAvg = ((previousAvg * previousCount) + totalCorrect) / updatedCount;
//
//        examAnalytics.setTotalSubmissions(updatedCount);
//        examAnalytics.setAverageScore(updatedAvg);
//        examAnalytics.setHighestScore(Math.max(examAnalytics.getHighestScore(), totalCorrect * 1.0));
//        examAnalytics.setLowestScore(previousCount == 0 ? totalCorrect * 1.0 : Math.min(examAnalytics.getLowestScore(), totalCorrect * 1.0));
//
//        examAnalyticsDao.update(examAnalytics);
//
//        exam.getEnrolledParticipantUsers().add(participantUser);
//        // add in completed participant users
//
//        examDao.save(exam);
//
//        participantUser.getParticipantUserExamResults().add(participantUserExamResult);
//        participantUser.getEnrolledExams().add(exam);
//        participantUser.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
//        participantUserDao.update(participantUser);
//
//        return participantUserExamResult.getId();
//    }


//    ---

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