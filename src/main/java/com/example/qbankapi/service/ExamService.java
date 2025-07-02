package com.example.qbankapi.service;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExamService {

//    private final ExamDao examDao;
//    private final SubjectDao subjectDao;
//    private final ExamAnalyticsDao examAnalyticsDao;
//    private final QuestionDao questionDao;
//    private final UserDao userDao;
//    private final UserAnalyticsDao userAnalyticsDao;
//    private final UserExamResultDao userExamResultDao;
//    private final UserAnswerDao userAnswerDao;
//    private final AdminDao adminDao;

//    @Transactional(readOnly = true)
//    public List<ExamDetailsDto> getAllExamsInDto(Long userId) {
//        User user = userDao.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format("User not found with id: %d", userId)));
//
//        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneId.of(user.getZoneId())).withZoneSameInstant(ZoneOffset.UTC);
//
//        return examDao.findAllByEnrollmentEndDate(nowUtc, user)
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
//    @Transactional
//    public void createExam(CreateExamRequestDto createExamRequestDto, Long adminId) {
//        Admin admin = adminDao.findById(adminId).orElseThrow(() -> new AdminNotFoundException(String.format("Admin not found with id", adminId)));
//        Subject subject = subjectDao.findById(createExamRequestDto.getSubjectId()).orElseThrow(() -> new SubjectNotFoundException(String.format("Subject not found with id", createExamRequestDto.getSubjectId())));
//
//        ExamAnalytics examAnalytics = new ExamAnalytics();
//        examAnalytics.setTotalSubmissions(0);
//        examAnalytics.setAverageScore(0D);
//        examAnalytics.setHighestScore(0D);
//        examAnalytics.setLowestScore(0D);
//
//        Integer totalMarks = (
//                (createExamRequestDto.getTotal1MarkQuestions() * 1) +
//                        (createExamRequestDto.getTotal2MarkQuestions() * 2) +
//                        (createExamRequestDto.getTotal3MarkQuestions() * 3) +
//                        (createExamRequestDto.getTotal4MarkQuestions() * 4) +
//                        (createExamRequestDto.getTotal5MarkQuestions() * 5) +
//                        (createExamRequestDto.getTotal6MarkQuestions() * 6)
//        );
//
//        ZoneId userZoneId = ZoneId.of(admin.getZoneId());
//
//        Exam exam = new Exam();
//        exam.setDescription(createExamRequestDto.getDescription());
//        exam.setTotalMarks(totalMarks);
//        exam.setSubject(subject);
//        exam.setQuestions(new ArrayList<>());
//        exam.setEnrolledUsers(List.of());
//        exam.setCompletedUsers(List.of());
//        exam.setAnalytics(examAnalytics);
//        exam.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC));
//        exam.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
//        exam.setEnrollmentStartDate(createExamRequestDto.getEnrollmentStartDate().atZone(userZoneId).withZoneSameInstant(ZoneOffset.UTC));
//        exam.setEnrollmentEndDate(createExamRequestDto.getEnrollmentEndDate().atZone(userZoneId).withZoneSameInstant(ZoneOffset.UTC));
//        exam.setExamStartDate(createExamRequestDto.getExamStartDate().atZone(userZoneId).withZoneSameInstant(ZoneOffset.UTC));
//        exam.setExamEndDate(createExamRequestDto.getExamEndDate().atZone(userZoneId).withZoneSameInstant(ZoneOffset.UTC));
//
//        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal1MarkQuestions(), Question.Complexity.EASY, 1);
//        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal2MarkQuestions(), Question.Complexity.EASY, 2);
//        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal3MarkQuestions(), Question.Complexity.MEDIUM, 3);
//        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal4MarkQuestions(), Question.Complexity.MEDIUM, 4);
//        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal5MarkQuestions(), Question.Complexity.HARD, 5);
//        addQuestionsIfAvailable(exam.getQuestions(), subject.getId(), createExamRequestDto.getTotal6MarkQuestions(), Question.Complexity.HARD, 6);
//
//        subject.getExams().add(exam);
//        examAnalytics.setExam(exam);
//
//        admin.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
//
//        adminDao.update(admin);
//        subjectDao.update(subject);
//        examAnalyticsDao.save(examAnalytics);
//        examDao.save(exam);
//    }
//
//    private void addQuestionsIfAvailable(List<Question> targetList, Long subjectId, Integer total, Question.Complexity complexity, int marks) {
//        if (total == null || total <= 0) return;
//
//        List<Question> questions = questionDao.findRandomQuestions(subjectId, total, complexity, marks);
//        if (questions.size() < total) {
//            throw new InSufficientQuestionsException(String.format("Not enough questions for marks: %d, complexity: %s", marks, complexity));
//        }
//
//        targetList.addAll(questions);
//    }
//
//    @Transactional(readOnly = true)
//    public ExamViewPageDto getFilteredExams(ExamFilterDto examFilterDto) {
//        if (examFilterDto.getSubjectId() == null || examFilterDto.getSubjectId() <= 0) examFilterDto.setSubjectId(0L);
//        if (examFilterDto.getSortBy() == null || examFilterDto.getSortBy().isBlank())
//            examFilterDto.setSortBy("createdAt");
//        if (examFilterDto.getSortOrder() == null || examFilterDto.getSortOrder().isBlank())
//            examFilterDto.setSortOrder("DESC");
//        if (examFilterDto.getPageSize() == null || examFilterDto.getPageSize() <= 0) examFilterDto.setPageSize(10);
//        if (examFilterDto.getPageSize() != 5 && examFilterDto.getPageSize() != 10 && examFilterDto.getPageSize() != 20)
//            examFilterDto.setPageSize(10);
//        if (examFilterDto.getPage() == null || examFilterDto.getPage() < 0) examFilterDto.setPage(0);
//
//        System.out.println("subjectId = " + Optional.ofNullable(examFilterDto.getSubjectId()));
//        System.out.println("sortBy = " + Optional.ofNullable(examFilterDto.getSortBy()));
//        System.out.println("sortOrder = " + Optional.ofNullable(examFilterDto.getSortOrder()));
//        System.out.println("size = " + Optional.ofNullable(examFilterDto.getPageSize()));
//        System.out.println("page = " + Optional.ofNullable(examFilterDto.getPage()));
//
//        return examDao.findFilteredExams(examFilterDto);
//    }
//
//    @Transactional(readOnly = true)
//    public ExamDto getExamInDtoById(Long id) {
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
//                .orElseThrow(() -> new UserNotFoundException(String.format("User not found with id: %d", userId)));
//
//        Exam exam = examDao.findById(submissionDto.getExamId())
//                .orElseThrow(() -> new ExamNotFoundException(String.format("Exam not found with id: %d", submissionDto.getExamId())));
//
//        ExamAnalytics examAnalytics = exam.getAnalytics();
//
//        int totalCorrect = 0;
//        int attempted = 0;
//        List<UserAnswer> userAnswers = new ArrayList<>();
//
//        for (Map.Entry<Long, Question.Option> entry : submissionDto.getAnswers().entrySet()) {
//            Long questionId = entry.getKey();
//            Optional<Question.Option> givenNullableAnswer = Optional.ofNullable(entry.getValue());
//
//            Question question = questionDao.findById(questionId)
//                    .orElseThrow(() -> new QuestionNotFoundException("Invalid Question ID: " + questionId));
//
//            Boolean isCorrect = false;
//            if (givenNullableAnswer.isPresent()) {
//                isCorrect = givenNullableAnswer.get().equals(question.getCorrectAnswer());
//            }
//
//            if (givenNullableAnswer.isPresent()) attempted++;
//            if (isCorrect) totalCorrect++;
//
//            UserAnswer userAnswer = new UserAnswer();
//            userAnswer.setQuestion(question);
//            userAnswer.setAnswerGiven(givenNullableAnswer.isPresent() ? givenNullableAnswer.get() : null);
//            userAnswer.setIsCorrect(isCorrect);
//            userAnswers.add(userAnswer);
//        }
//
//        Double accuracy = attempted > 0 ? ((double) totalCorrect / attempted) * 100.0 : 0.0;
//        UserAnalytics userAnalytics = new UserAnalytics();
//        userAnalytics.setAttemptedQuestions(attempted);
//        userAnalytics.setCorrectAnswers(totalCorrect);
//        userAnalytics.setAccuracy(accuracy);
//        userAnalyticsDao.save(userAnalytics);
//
//        UserExamResult userExamResult = new UserExamResult();
//        userExamResult.setSubmittedAt(LocalDateTime.now());
//        userExamResult.setTotalScore(totalCorrect);
//        userExamResult.setUser(user);
//        userExamResult.setExam(exam);
//        userExamResult.setAnalytics(userAnalytics);
//        userExamResult.setAnswers(userAnswers);
//
//        userAnswers.forEach(ans -> ans.setUserExamResult(userExamResult));
//        userExamResultDao.save(userExamResult);
//
//        userAnalytics.setUserExamResult(userExamResult);
//        userAnalyticsDao.update(userAnalytics);
//
//        userAnswers.stream().forEach(userAnswer -> {
//            userAnswer.setUserExamResult(userExamResult);
//            userAnswerDao.save(userAnswer);
//        });
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
//        user.getUserExamResults().add(userExamResult);
//        user.getEnrolledExams().add(exam);
//        user.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
//        userDao.update(user);
//
//        return userExamResult.getId();
//    }
//
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
//
//    @Transactional(readOnly = true)
//    public List<ExamDetailsDto> getExamsInDtoBySubjectId(Long subjectId, Long userId) {
//        User user = userDao.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format("User not found with id %d", userId)));
//
//        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneId.of(user.getZoneId())).withZoneSameInstant(ZoneOffset.UTC);
//
//        return examDao.findAllByEnrollmentEndDateAndSubjectId(nowUtc, subjectId, user)
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

}