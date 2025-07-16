package com.example.qbankapi.service;

import com.example.qbankapi.dao.*;
import com.example.qbankapi.dto.model.AllExamFilterDto;
import com.example.qbankapi.dto.model.ExamDto;
import com.example.qbankapi.dto.model.InstructorCreatedExamsFilterDto;
import com.example.qbankapi.dto.request.CreateExamRequestDto;
import com.example.qbankapi.dto.view.ExamAnalyticsViewDto;
import com.example.qbankapi.dto.view.ExamPageViewDto;
import com.example.qbankapi.dto.view.ExamViewDto;
import com.example.qbankapi.dto.view.ParticipantUserExamViewDto;
import com.example.qbankapi.entity.*;
import com.example.qbankapi.exception.base.impl.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
//    private final BaseUserDao baseUserDao;
//    private final UserAnalyticsDao userAnalyticsDao;
//    private final UserExamResultDao userExamResultDao;
//    private final UserAnswerDao userAnswerDao;

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
        examAnalytics.setHighestScore(0D);
        examAnalytics.setLowestScore(0D);

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

        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneId.of(participantUser.getZoneId())).withZoneSameInstant(ZoneOffset.UTC);

        System.out.println("---");
        System.out.println(participantUser.getExamEnrollments());

        return examDao.findAllByEnrollmentStartEndDate(nowUtc).stream().map(exam -> ParticipantUserExamViewDto.builder().id(exam.getId()).description(exam.getDescription()).totalMarks(exam.getTotalMarks()).subjectName(exam.getSubject().getName()).questionsCount(exam.getQuestions().size()).enrollmentCount(exam.getParticipantEnrollments().size()).isEnrolled(exam.getParticipantEnrollments().stream().anyMatch(participantUserExamEnrollment -> participantUserExamEnrollment.getParticipantUser().getId().equals(userId))).build()).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ParticipantUserExamViewDto> getExamsInDtoBySubjectId(Long subjectId, Long userId) {
        ParticipantUser participantUser = participantUserDao.findById(userId).orElseThrow(() -> new ParticipantUserNotFoundException(String.format("User not found with id: %d", userId)));

        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneId.of(participantUser.getZoneId())).withZoneSameInstant(ZoneOffset.UTC);

        System.out.println("---");
        System.out.println(participantUser.getExamEnrollments());

        return examDao.findAllByEnrollmentStartEndDateAndSubjectId(nowUtc, subjectId).stream().map(exam -> ParticipantUserExamViewDto.builder().id(exam.getId()).description(exam.getDescription()).totalMarks(exam.getTotalMarks()).subjectName(exam.getSubject().getName()).questionsCount(exam.getQuestions().size()).enrollmentCount(exam.getParticipantEnrollments().size()).isEnrolled(exam.getParticipantEnrollments().stream().anyMatch(participantUserExamEnrollment -> participantUserExamEnrollment.getParticipantUser().getId().equals(userId))).build()).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ExamDto getExamInDtoByEnrollmentId(Long examEnrollmentId) {
        ParticipantUserExamEnrollment participantUserExamEnrollment = participantUserExamEnrollmentDao.findById(examEnrollmentId).orElseThrow(() -> new ParticipantUserExamEnrollmentNotFoundException(String.format("Participant user exam enrollment not found with id [%d]", examEnrollmentId)));

//      validate

        Exam exam = participantUserExamEnrollment.getExam();
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

    @Transactional
    public void enrollExam(Long userId, Long examId) {
        ParticipantUser participantUser = participantUserDao.findById(userId).orElseThrow(() -> new ParticipantUserNotFoundException(String.format("Participant User not found with id [%d]", userId)));

        Exam exam = examDao.findById(examId).orElseThrow(() -> new ExamNotFoundException(String.format("Exam not found with id [%d]", examId)));

        ZoneId userZone = ZoneId.of(participantUser.getZoneId());

        ZonedDateTime currentTimeInUserZone = ZonedDateTime.now(userZone);
        ZonedDateTime examEnrollmentStartInUserZone = exam.getEnrollmentStartDate().withZoneSameInstant(userZone);
        ZonedDateTime examEnrollmentEndInUserZone = exam.getEnrollmentEndDate().withZoneSameInstant(userZone);

        if (!(currentTimeInUserZone.isAfter(examEnrollmentStartInUserZone) && currentTimeInUserZone.isBefore(examEnrollmentEndInUserZone))) {
            throw new AccessDeniedException(String.format("Exam with id [%d] has not started yet in your time zone [%s]", examId, userZone));
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

}