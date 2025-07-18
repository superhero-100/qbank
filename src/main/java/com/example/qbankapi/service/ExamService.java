package com.example.qbankapi.service;

import com.example.qbankapi.dao.*;
import com.example.qbankapi.dto.model.*;
import com.example.qbankapi.dto.request.CreateExamRequestDto;
import com.example.qbankapi.dto.view.*;
import com.example.qbankapi.entity.*;
import com.example.qbankapi.exception.base.impl.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamDao examDao;
    private final InstructorUserDao instructorUserDao;
    private final AdminUserDao adminUserDao;
    private final SubjectDao subjectDao;
    private final ExamAnalyticsDao examAnalyticsDao;
    private final QuestionDao questionDao;
    private final ParticipantUserDao participantUserDao;
    private final ParticipantUserExamEnrollmentDao participantUserExamEnrollmentDao;
    private final ParticipantUserExamQuestionAnswerDao participantUserExamQuestionAnswerDao;
    private final ParticipantUserExamSubmissionDao participantUserExamSubmissionDao;
    private final ParticipantUserExamAnalyticsDao participantUserExamAnalyticsDao;
    private final ParticipantUserExamResultDao participantUserExamResultDao;
    private final Random random;
    private final List<String> colorPalette = List.of("#0d6efd", "#6610f2", "#6f42c1", "#d63384", "#dc3545", "#fd7e14", "#ffc107", "#198754", "#20c997", "#0dcaf0", "#6c757d");

    @Transactional(readOnly = true)
    public ExamPageViewDto getFilteredExamsForAdmin(AllExamFilterDto allExamFilterDto) {
        log.info("Invoked getFilteredExams with initial filter [{}]", allExamFilterDto);

        normalizeFilterDto(allExamFilterDto);

        ExamPageViewDto examPageViewDto = examDao.findFilteredExams(allExamFilterDto);
        log.info("Retrieved [{}] exams with applied filters", examPageViewDto.getExams().size());

        return examPageViewDto;
    }

    @Transactional(readOnly = true)
    public ExamPageViewDto getFilteredExamsForInstructor(AllExamFilterDto allExamFilterDto, Long instructorId) {
        log.info("Invoked getFilteredExams with initial filter [{}]", allExamFilterDto);

        normalizeFilterDto(allExamFilterDto);

        InstructorUser instructorUser = instructorUserDao.findById(instructorId)
                .orElseThrow(() -> new InstructorUserNotFoundException(String.format("Instructor user not found with id [%d]", instructorId)));

        Set<Long> assignedSubjectIds = instructorUser.getAssignedSubjects()
                .stream()
                .map(Subject::getId)
                .collect(Collectors.toSet());

        if (allExamFilterDto.getSubjectId() != 0 && !assignedSubjectIds.contains(allExamFilterDto.getSubjectId())) {
            allExamFilterDto.setSubjectId(0L);
        }

        ExamPageViewDto examPageViewDto = examDao.findFilteredExamsBySubjectsIn(allExamFilterDto, assignedSubjectIds);
        log.info("Retrieved [{}] exams with applied filters", examPageViewDto.getExams().size());

        return examPageViewDto;
    }

    @Transactional(readOnly = true)
    public ExamPageViewDto getFilteredInstructorCreatedExamsForAdmin(InstructorCreatedExamsFilterDto instructorCreatedExamsFilterDto, Long instructorId) {
        log.info("Invoked getFilteredInstructorCreatedExams with initial filter [{}]", instructorCreatedExamsFilterDto);

        instructorUserDao.findById(instructorId)
                .orElseThrow(() -> new InstructorUserNotFoundException(String.format("Instructor user not found with id [%d]", instructorId)));

        normalizeFilterDto(instructorCreatedExamsFilterDto);

        ExamPageViewDto examPageViewDto = examDao.findFilteredInstructorCreatedExams(instructorCreatedExamsFilterDto, instructorId);
        log.info("Retrieved [{}] exams with applied filters", examPageViewDto.getExams().size());

        return examPageViewDto;
    }

    @Transactional(readOnly = true)
    public ExamPageViewDto getFilteredInstructorCreatedExamsForInstructor(InstructorCreatedExamsFilterDto instructorCreatedExamsFilterDto, Long instructorId) {
        log.info("Invoked getFilteredInstructorCreatedExamsForInstructor with initial filter [{}]", instructorCreatedExamsFilterDto);

        instructorUserDao.findById(instructorId)
                .orElseThrow(() -> new InstructorUserNotFoundException(String.format("Instructor user not found with id [%d]", instructorId)));

        normalizeFilterDto(instructorCreatedExamsFilterDto);

        ExamPageViewDto examPageViewDto = examDao.findFilteredInstructorCreatedExams(instructorCreatedExamsFilterDto, instructorId);
        log.info("Retrieved [{}] exams with applied filters", examPageViewDto.getExams().size());

        return examPageViewDto;
    }

    @Transactional(readOnly = true)
    public ExamAnalyticsViewDto getExamAnalytics(Long examId, String currentUserZoneId) {
        Exam exam = examDao.findById(examId)
                .orElseThrow(() -> new ExamNotFoundException(String.format("Exam not found with id [%d]", examId)));
        log.debug("Fetched exam from DB with id [{}]", examId);

        return ExamAnalyticsViewDto.builder()
                .examId(exam.getId())
                .examDescription(exam.getDescription())
                .subjectName(exam.getSubject().getName())
                .totalMarks(exam.getTotalMarks())
                .totalParticipants(exam.getParticipantEnrollments().size())
                .totalSubmissions(exam.getParticipantUserExamSubmissions().size())
                .averageScore(exam.getExamAnalytics().getAverageScore())
                .highestScore(exam.getExamAnalytics().getHighestScore())
                .lowestScore(exam.getExamAnalytics().getLowestScore())
                .totalQuestions(exam.getQuestions().size())
                .createdBy(exam.getCreatedByBaseUser().getUsername())
                .createdAt(exam.getCreatedAt().withZoneSameInstant(ZoneId.of(currentUserZoneId)))
//                ----
                .enrollmentStartDate(exam.getEnrollmentStartDate().withZoneSameInstant(ZoneId.of(currentUserZoneId)))
                .enrollmentEndDate(exam.getEnrollmentEndDate().withZoneSameInstant(ZoneId.of(currentUserZoneId)))
                .examStartDate(exam.getExamStartDate().withZoneSameInstant(ZoneId.of(currentUserZoneId)))
                .examEndDate(exam.getExamEndDate().withZoneSameInstant(ZoneId.of(currentUserZoneId)))
//                ----
                .build();
    }

    @Transactional
    public void adminCreateExam(CreateExamRequestDto createExamRequestDto, Long adminUserId) {
        log.info("Starting exam creation by admin user id [{}]", adminUserId);

        AdminUser adminUser = adminUserDao.findById(adminUserId)
                .orElseThrow(() -> new AdminUserNotFoundException(String.format("Admin user not found with id [%d]", adminUserId)));

        Subject subject = subjectDao.findById(createExamRequestDto.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException(String.format("Subject not found with id [%d]", createExamRequestDto.getSubjectId())));

        ExamAnalytics examAnalytics = createExamAnalytics();

        examAnalyticsDao.save(examAnalytics);

        Exam exam = createExam(createExamRequestDto, subject, examAnalytics, adminUser, adminUser.getZoneId());

        examDao.save(exam);
    }

    @Transactional
    public void instructorCreateExam(CreateExamRequestDto createExamRequestDto, Long instructorId) {
        log.info("Starting exam creation by instructor user id [{}]", instructorId);

        InstructorUser instructorUser = instructorUserDao.findById(instructorId)
                .orElseThrow(() -> new InstructorUserNotFoundException(String.format("Instructor user not found with id [%d]", instructorId)));

        Subject subject = subjectDao.findById(createExamRequestDto.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException(String.format("Subject not found with id [%d]", createExamRequestDto.getSubjectId())));

        if (!instructorUser.getAssignedSubjects().contains(subject)) {
            throw new AccessDeniedException(String.format("Access denied subject with id [%d] not assigned to instructor with id [%d]", subject.getId(), instructorId));
        }

        ExamAnalytics examAnalytics = createExamAnalytics();

        examAnalyticsDao.save(examAnalytics);

        Exam exam = createExam(createExamRequestDto, subject, examAnalytics, instructorUser, instructorUser.getZoneId());

        examDao.save(exam);
    }

    private Exam createExam(CreateExamRequestDto createExamRequestDto, Subject subject, ExamAnalytics examAnalytics, BaseUser baseUser, String zoneId) {
        Exam exam = new Exam();

        exam.setDescription(createExamRequestDto.getDescription());
        exam.setTotalMarks((createExamRequestDto.getTotal1MarkQuestions() + (createExamRequestDto.getTotal2MarkQuestions() * 2) + (createExamRequestDto.getTotal3MarkQuestions() * 3) + (createExamRequestDto.getTotal4MarkQuestions() * 4) + (createExamRequestDto.getTotal5MarkQuestions() * 5) + (createExamRequestDto.getTotal6MarkQuestions() * 6)));
        exam.setSubject(subject);
        exam.setQuestions(new ArrayList<>());
        exam.setParticipantEnrollments(List.of());
        exam.setParticipantUserExamSubmissions(List.of());
        exam.setParticipantUserExamResults(List.of());
        exam.setExamAnalytics(examAnalytics);
        exam.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC));

        exam.setEnrollmentStartDate(createExamRequestDto.getEnrollmentStartDate().atZone(ZoneId.of(zoneId)).withZoneSameInstant(ZoneOffset.UTC));
        exam.setEnrollmentEndDate(createExamRequestDto.getEnrollmentEndDate().atZone(ZoneId.of(zoneId)).withZoneSameInstant(ZoneOffset.UTC));
        exam.setExamStartDate(createExamRequestDto.getExamStartDate().atZone(ZoneId.of(zoneId)).withZoneSameInstant(ZoneOffset.UTC));
        exam.setExamEndDate(createExamRequestDto.getExamEndDate().atZone(ZoneId.of(zoneId)).withZoneSameInstant(ZoneOffset.UTC));

        exam.setCreationZone(zoneId);
        exam.setCreatedByBaseUser(baseUser);

        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal1MarkQuestions(), Question.Complexity.EASY, 1);
        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal2MarkQuestions(), Question.Complexity.EASY, 2);
        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal3MarkQuestions(), Question.Complexity.MEDIUM, 3);
        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal4MarkQuestions(), Question.Complexity.MEDIUM, 4);
        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal5MarkQuestions(), Question.Complexity.HARD, 5);
        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal6MarkQuestions(), Question.Complexity.HARD, 6);

        return exam;
    }

    private ExamAnalytics createExamAnalytics() {
        ExamAnalytics examAnalytics = new ExamAnalytics();

        examAnalytics.setEnrollmentsCount(0);
        examAnalytics.setSubmissionsCount(0);
        examAnalytics.setAverageScore(0D);
        examAnalytics.setHighestScore(0);
        examAnalytics.setLowestScore(0);

        return examAnalytics;
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

    @Transactional(readOnly = true)
    public List<ParticipantUserExamViewDto> getAllExamsInDto(Long userId) {
        ParticipantUser participantUser = participantUserDao.findById(userId).orElseThrow(() -> new ParticipantUserNotFoundException(String.format("User not found with id: %d", userId)));

        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneOffset.UTC);
        ZoneId userZoneId = ZoneId.of(participantUser.getZoneId());

        return examDao.findAllByEnrollmentStartEndDate(nowUtc).stream().map(exam -> ParticipantUserExamViewDto.builder().id(exam.getId()).description(exam.getDescription()).totalMarks(exam.getTotalMarks()).subjectName(exam.getSubject().getName()).questionsCount(exam.getQuestions().size()).enrollmentCount(exam.getParticipantEnrollments().size()).enrollmentStartDate(exam.getEnrollmentStartDate().withZoneSameInstant(userZoneId)).enrollmentEndDate(exam.getEnrollmentEndDate().withZoneSameInstant(userZoneId)).isEnrolled(exam.getParticipantEnrollments().stream().anyMatch(participantUserExamEnrollment -> participantUserExamEnrollment.getParticipantUser().getId().equals(userId))).build()).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ParticipantUserExamViewDto> getExamsInDtoBySubjectId(Long subjectId, Long userId) {
        ParticipantUser participantUser = participantUserDao.findById(userId).orElseThrow(() -> new ParticipantUserNotFoundException(String.format("User not found with id: %d", userId)));

        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneOffset.UTC);
        ZoneId userZoneId = ZoneId.of(participantUser.getZoneId());

        return examDao.findAllByEnrollmentStartEndDateAndSubjectId(nowUtc, subjectId).stream().map(exam -> ParticipantUserExamViewDto.builder().id(exam.getId()).description(exam.getDescription()).totalMarks(exam.getTotalMarks()).subjectName(exam.getSubject().getName()).questionsCount(exam.getQuestions().size()).enrollmentCount(exam.getParticipantEnrollments().size()).enrollmentStartDate(exam.getExamStartDate().withZoneSameInstant(userZoneId)).enrollmentEndDate(exam.getEnrollmentEndDate().withZoneSameInstant(userZoneId)).isEnrolled(exam.getParticipantEnrollments().stream().anyMatch(participantUserExamEnrollment -> participantUserExamEnrollment.getParticipantUser().getId().equals(userId))).build()).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ExamDto getExamInDtoByEnrollmentId(Long examEnrollmentId, Long participantId) {
        ParticipantUserExamEnrollment participantUserExamEnrollment = participantUserExamEnrollmentDao.findById(examEnrollmentId).orElseThrow(() -> new ParticipantUserExamEnrollmentNotFoundException(String.format("Participant user exam enrollment not found with id [%d]", examEnrollmentId)));

        ParticipantUser participantUser = participantUserExamEnrollment.getParticipantUser();

        Exam exam = participantUserExamEnrollment.getExam();

        if (!participantUser.getId().equals(participantId)) {
            log.warn("Access violation: participantId [{}] attempted to access enrollment belonging to participantId [{}]", participantId, participantUser.getId());

            throw new AccessDeniedException(String.format("Access denied: Your account does not have permission to access this exam enrollment [%d].", examEnrollmentId));
        }

        if (participantUserExamEnrollment.getExamAttemptStatus().equals(ParticipantUserExamEnrollment.ExamAttemptStatus.SUBMITTED)) {
            log.warn("Duplicate submission attempt: participantId [{}], examId [{}], enrollmentId [{}] has already been submitted.", participantUserExamEnrollment.getParticipantUser().getId(), participantUserExamEnrollment.getExam().getId(), participantUserExamEnrollment.getId());

            throw new ParticipantUserExamSubmissionAlreadyExistsException(String.format("Exam submission already exists for enrollmentId [%d].", participantUserExamEnrollment.getId()));
        }

        ZoneId userZone = ZoneId.of(participantUser.getZoneId());

        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneOffset.UTC);

        if (!(nowUtc.isAfter(exam.getExamStartDate()) && nowUtc.isBefore(exam.getExamEndDate()))) {
            log.warn("Access denied: Current time [{}] is outside the valid exam window [{} - {}] for user zone [{}]", nowUtc, exam.getExamStartDate(), exam.getExamEndDate(), userZone);

            throw new AccessDeniedException(String.format("Exam with id [%d] has not started", exam.getId()));
        }

        return ExamDto.builder()
                .id(exam.getId())
                .description(exam.getDescription())
                .totalMarks(exam.getTotalMarks())
                .subjectName(exam.getSubject().getName())
                .totalQuestions(exam.getQuestions().size())
                .totalEnrolledUsers(exam.getParticipantEnrollments().size())
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
    public void saveUserExamSubmission(ExamSubmissionDto submissionDto) {
        ParticipantUserExamEnrollment participantUserExamEnrollment = participantUserExamEnrollmentDao.findById(submissionDto.getExamEnrollmentId()).orElseThrow(() -> new ParticipantUserExamEnrollmentNotFoundException(String.format("ParticipantUserExamEnrollment not found with ID [%d]", submissionDto.getExamEnrollmentId())));
        participantUserExamEnrollment.setExamAttemptStatus(ParticipantUserExamEnrollment.ExamAttemptStatus.SUBMITTED);
        participantUserExamEnrollmentDao.update(participantUserExamEnrollment);

        ParticipantUser participantUser = participantUserExamEnrollment.getParticipantUser();
        Exam exam = participantUserExamEnrollment.getExam();

        ParticipantUserExamSubmission participantUserExamSubmission = new ParticipantUserExamSubmission();

        participantUserExamSubmission.setParticipantUser(participantUser);
        participantUserExamSubmission.setExam(exam);
        participantUserExamSubmission.setParticipantUserExamEnrollment(participantUserExamEnrollment);
        participantUserExamSubmission.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC));
        participantUserExamSubmission.setCreationZone(participantUser.getZoneId());

        participantUserExamSubmissionDao.save(participantUserExamSubmission);

        int attempted = 0;
        int totalCorrect = 0;

        for (Map.Entry<Long, Question.Option> entry : submissionDto.getAnswers().entrySet()) {
            Long questionId = entry.getKey();
            Optional<Question.Option> givenNullableAnswer = Optional.ofNullable(entry.getValue());

            Question question = questionDao.findById(questionId).orElseThrow(() -> new QuestionNotFoundException("Invalid Question ID: " + questionId));

            boolean isCorrect = false;
            if (givenNullableAnswer.isPresent()) {
                isCorrect = givenNullableAnswer.get().equals(question.getCorrectAnswer());
            }

            if (givenNullableAnswer.isPresent()) attempted++;
            if (isCorrect) totalCorrect++;

            ParticipantUserExamQuestionAnswer participantUserExamQuestionAnswer = new ParticipantUserExamQuestionAnswer();

            participantUserExamQuestionAnswer.setQuestion(question);
            participantUserExamQuestionAnswer.setAnswerGiven(givenNullableAnswer.orElse(null));
            participantUserExamQuestionAnswer.setIsCorrect(isCorrect);
            participantUserExamQuestionAnswer.setParticipantUserExamSubmission(participantUserExamSubmission);

            participantUserExamQuestionAnswerDao.save(participantUserExamQuestionAnswer);
        }

        Double accuracy = attempted > 0 ? ((double) totalCorrect / attempted) * 100.0 : 0.0;

        ParticipantUserExamAnalytics participantUserExamAnalytics = new ParticipantUserExamAnalytics();

        participantUserExamAnalytics.setAttemptedQuestions(attempted);
        participantUserExamAnalytics.setCorrectAnswers(totalCorrect);
        participantUserExamAnalytics.setAccuracy(accuracy);

        participantUserExamAnalyticsDao.save(participantUserExamAnalytics);

        ParticipantUserExamResult participantUserExamResult = new ParticipantUserExamResult();

        participantUserExamResult.setTotalScore(totalCorrect);
        participantUserExamResult.setParticipantUser(participantUser);
        participantUserExamResult.setExam(exam);
        participantUserExamResult.setParticipantUserExamAnalytics(participantUserExamAnalytics);
        participantUserExamResult.setParticipantUserExamSubmission(participantUserExamSubmission);

        participantUserExamResultDao.save(participantUserExamResult);

        ExamAnalytics examAnalytics = exam.getExamAnalytics();

        int previousCount = examAnalytics.getSubmissionsCount();
        double previousAvg = examAnalytics.getAverageScore();

        int updatedCount = previousCount + 1;
        double updatedAvg = ((previousAvg * previousCount) + totalCorrect) / updatedCount;
        int updatedHighestScore = Math.max(examAnalytics.getHighestScore(), totalCorrect);
        int updatedLowestScore = previousCount == 0 ? totalCorrect : Math.min(examAnalytics.getLowestScore(), totalCorrect);

        examAnalytics.setSubmissionsCount(updatedCount);
        examAnalytics.setAverageScore(updatedAvg);
        examAnalytics.setHighestScore(updatedHighestScore);
        examAnalytics.setLowestScore(updatedLowestScore);

        examAnalyticsDao.update(examAnalytics);
    }

    @Transactional
    public void enrollExam(Long userId, Long examId) {
        ParticipantUser participantUser = participantUserDao.findById(userId).orElseThrow(() -> new ParticipantUserNotFoundException(String.format("Participant User not found with id [%d]", userId)));

        Exam exam = examDao.findById(examId).orElseThrow(() -> new ExamNotFoundException(String.format("Exam not found with id [%d]", examId)));

        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneOffset.UTC);

        if (!(nowUtc.isAfter(exam.getEnrollmentStartDate()) && nowUtc.isBefore(exam.getEnrollmentEndDate()))) {
            throw new AccessDeniedException(String.format("Exam with id [%d] has not started", examId));
        }

        ParticipantUserExamEnrollment participantUserExamEnrollment = new ParticipantUserExamEnrollment();

        participantUserExamEnrollment.setExam(exam);
        participantUserExamEnrollment.setParticipantUser(participantUser);
        participantUserExamEnrollment.setExamAttemptStatus(ParticipantUserExamEnrollment.ExamAttemptStatus.NOT_ATTEMPTED);

        participantUserExamEnrollmentDao.save(participantUserExamEnrollment);
    }

    private void normalizeFilterDto(AllExamFilterDto allExamFilterDto) {
        if (allExamFilterDto.getSubjectId() == null || allExamFilterDto.getSubjectId() <= 0) {
            log.debug("Invalid or missing subjectId. Defaulting to 0");
            allExamFilterDto.setSubjectId(0L);
        }
        if (allExamFilterDto.getSortBy() == null || allExamFilterDto.getSortBy().isBlank()) {
            log.debug("Missing sortBy. Defaulting to 'createdAt'");
            allExamFilterDto.setSortBy("createdAt");
        }
        if (allExamFilterDto.getSortOrder() == null || allExamFilterDto.getSortOrder().isBlank()) {
            log.debug("Missing sortOrder. Defaulting to 'DESC'");
            allExamFilterDto.setSortOrder("DESC");
        }
        if (allExamFilterDto.getPageSize() == null || allExamFilterDto.getPageSize() <= 0) {
            log.debug("Missing or invalid pageSize. Defaulting to 10");
            allExamFilterDto.setPageSize(10);
        }
        if (allExamFilterDto.getPageSize() != 5 && allExamFilterDto.getPageSize() != 10 && allExamFilterDto.getPageSize() != 20) {
            log.warn("Unsupported pageSize: {}. Resetting to default (10)", allExamFilterDto.getPageSize());
            allExamFilterDto.setPageSize(10);
        }
        if (allExamFilterDto.getPage() == null || allExamFilterDto.getPage() < 0) {
            log.debug("Missing or invalid page number. Defaulting to 0");
            allExamFilterDto.setPage(0);
        }

        log.info("Final applied filters - subjectId: {}, sortBy: {}, sortOrder: {}, pageSize: {}, page: {}",
                allExamFilterDto.getSubjectId(),
                allExamFilterDto.getSortBy(),
                allExamFilterDto.getSortOrder(),
                allExamFilterDto.getPageSize(),
                allExamFilterDto.getPage());
    }

    private void normalizeFilterDto(InstructorCreatedExamsFilterDto instructorCreatedExamsFilterDto) {
        if (instructorCreatedExamsFilterDto.getSubjectId() == null || instructorCreatedExamsFilterDto.getSubjectId() <= 0) {
            log.debug("Invalid or missing subjectId. Defaulting to 0");
            instructorCreatedExamsFilterDto.setSubjectId(0L);
        }
        if (instructorCreatedExamsFilterDto.getSortBy() == null || instructorCreatedExamsFilterDto.getSortBy().isBlank()) {
            log.debug("Missing sortBy. Defaulting to 'createdAt'");
            instructorCreatedExamsFilterDto.setSortBy("createdAt");
        }
        if (instructorCreatedExamsFilterDto.getSortOrder() == null || instructorCreatedExamsFilterDto.getSortOrder().isBlank()) {
            log.debug("Missing sortOrder. Defaulting to 'DESC'");
            instructorCreatedExamsFilterDto.setSortOrder("DESC");
        }
        if (instructorCreatedExamsFilterDto.getPageSize() == null || instructorCreatedExamsFilterDto.getPageSize() <= 0) {
            log.debug("Missing or invalid pageSize. Defaulting to 10");
            instructorCreatedExamsFilterDto.setPageSize(10);
        }
        if (instructorCreatedExamsFilterDto.getPageSize() != 5 && instructorCreatedExamsFilterDto.getPageSize() != 10 && instructorCreatedExamsFilterDto.getPageSize() != 20) {
            log.warn("Unsupported pageSize: {}. Resetting to default (10)", instructorCreatedExamsFilterDto.getPageSize());
            instructorCreatedExamsFilterDto.setPageSize(10);
        }
        if (instructorCreatedExamsFilterDto.getPage() == null || instructorCreatedExamsFilterDto.getPage() < 0) {
            log.debug("Missing or invalid page number. Defaulting to 0");
            instructorCreatedExamsFilterDto.setPage(0);
        }

        log.info("Final applied filters - subjectId: {}, sortBy: {}, sortOrder: {}, pageSize: {}, page: {}",
                instructorCreatedExamsFilterDto.getSubjectId(),
                instructorCreatedExamsFilterDto.getSortBy(),
                instructorCreatedExamsFilterDto.getSortOrder(),
                instructorCreatedExamsFilterDto.getPageSize(),
                instructorCreatedExamsFilterDto.getPage());
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getUpcomingExams(Long participantUserId) {
        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneOffset.UTC);
        List<ParticipantUserExamEnrollment> participantUserExamEnrollments = participantUserExamEnrollmentDao.findAllByParticipantUserIdAndNowIsBeforeExamEnd(participantUserId, nowUtc);

        return participantUserExamEnrollments.stream().map(enrollment -> {
            Exam exam = enrollment.getExam();
            Map<String, Object> event = new HashMap<>();

            String subjectName = exam.getSubject().getName();
            String description = exam.getDescription();

            event.put("title", subjectName + " - " + description);
            event.put("start", exam.getExamStartDate().withZoneSameInstant(ZoneOffset.UTC).toString());
            event.put("end", exam.getExamEndDate().withZoneSameInstant(ZoneOffset.UTC).toString());
            event.put("examEnrollmentId", enrollment.getId());

            String randomColor = colorPalette.get(random.nextInt(colorPalette.size()));
            event.put("backgroundColor", randomColor);
            event.put("textColor", "#ffffff");

            return event;
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TodayExamViewDto> getTodayExams(Long participantUserId) {
        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneOffset.UTC);
        List<ParticipantUserExamEnrollment> participantUserExamEnrollments = participantUserExamEnrollmentDao.findAllByParticipantUserIdAndNowIsBetweenExamStartEnd(participantUserId, nowUtc);
        return participantUserExamEnrollments.stream().map(participantUserExamEnrollment -> {
            Exam exam = participantUserExamEnrollment.getExam();
            return TodayExamViewDto.builder()
                    .enrollmentId(participantUserExamEnrollment.getId())
                    .description(exam.getDescription())
                    .subjectName(exam.getSubject().getName())
                    .totalMarks(exam.getTotalMarks())
                    .questionsCount(exam.getQuestions().size())
                    .enrollmentCount(exam.getParticipantEnrollments().size())
                    .build();
        }).collect(Collectors.toList());
    }

}