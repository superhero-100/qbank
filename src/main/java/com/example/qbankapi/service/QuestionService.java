package com.example.qbankapi.service;

import com.example.qbankapi.dao.*;
import com.example.qbankapi.dto.model.InstructorCreatedQuestionsFilterDto;
import com.example.qbankapi.dto.model.QuestionDto;
import com.example.qbankapi.dto.model.AllQuestionFilterDto;
import com.example.qbankapi.dto.request.AddQuestionRequestDto;
import com.example.qbankapi.dto.request.UpdateQuestionRequestDto;
import com.example.qbankapi.dto.view.QuestionAnalyticsViewDto;
import com.example.qbankapi.dto.view.QuestionPageViewDto;
import com.example.qbankapi.entity.*;
import com.example.qbankapi.exception.base.impl.*;
import lombok.RequiredArgsConstructor;
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
public class QuestionService {

    private final QuestionDao questionDao;
    private final InstructorUserDao instructorUserDao;
    private final SubjectDao subjectDao;
    private final AdminUserDao adminUserDao;
//    private final BaseUserDao baseUserDao;

    @Transactional(readOnly = true)
    public QuestionPageViewDto getFilteredQuestionsForAdmin(AllQuestionFilterDto allQuestionFilterDto) {
        log.info("Invoked getFilteredQuestionsForAdmin with initial filter [{}]", allQuestionFilterDto);

        normalizeFilterDto(allQuestionFilterDto);

        QuestionPageViewDto questionViewPageDto = questionDao.findFilteredQuestions(allQuestionFilterDto);
        log.info("Retrieved [{}] questions with applied filters", questionViewPageDto.getQuestions().size());

        return questionViewPageDto;
    }

    @Transactional(readOnly = true)
    public QuestionPageViewDto getFilteredInstructorCreatedQuestionsForAdmin(InstructorCreatedQuestionsFilterDto instructorCreatedQuestionsFilterDto, Long instructorId) {
        log.info("Invoked getFilteredInstructorCreatedQuestionsForAdmin with initial filter: {}", instructorCreatedQuestionsFilterDto);

        instructorUserDao.findById(instructorId)
                .orElseThrow(() -> new InstructorUserNotFoundException(String.format("Instructor user not found with id [%d]", instructorId)));

        normalizeFilterDto(instructorCreatedQuestionsFilterDto);

        QuestionPageViewDto questionViewPageDto = questionDao.findFilteredInstructorCreatedQuestions(instructorCreatedQuestionsFilterDto, instructorId);
        log.info("Retrieved [{}] questions with applied filters", questionViewPageDto.getQuestions().size());

        return questionViewPageDto;
    }

    @Transactional(readOnly = true)
    public QuestionAnalyticsViewDto getQuestionAnalytics(Long questionId) {
        Question question = questionDao.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(String.format("Question not found with id: %d", questionId)));
        log.debug("Fetched question from DB with id [{}]", questionId);

        List<ParticipantUserExamQuestionAnswer> participantUserExamQuestionAnswers = question.getParticipantUserExamQuestionAnswers();

        int totalAttempts = participantUserExamQuestionAnswers.size();
        int correctAttempts = (int) participantUserExamQuestionAnswers.stream().filter(ParticipantUserExamQuestionAnswer::getIsCorrect).count();
        int incorrectAttempts = totalAttempts - correctAttempts;

        double percentCorrect = totalAttempts > 0 ? (correctAttempts * 100.0) / totalAttempts : 0.0;
        double percentIncorrect = totalAttempts > 0 ? (incorrectAttempts * 100.0) / totalAttempts : 0.0;

        return QuestionAnalyticsViewDto.builder()
                .questionId(question.getId())
                .questionText(question.getText())
                .subjectName(question.getSubject().getName())
                .complexity(question.getComplexity())
                .marks(question.getMarks())
                .isActive(question.getIsActive())
                .totalAttempts(totalAttempts)
                .correctAttempts(correctAttempts)
                .incorrectAttempts(incorrectAttempts)
                .percentCorrect(percentCorrect)
                .percentIncorrect(percentIncorrect)
                .createdAt(question.getCreatedAt().withZoneSameInstant(ZoneId.of(question.getCreationZone())))
                .creationZone(question.getCreationZone())
                .createdByUsername(question.getCreatedByBaseUser().getUsername())
                .build();
    }


    @Transactional
    public void adminAddQuestion(AddQuestionRequestDto addQuestionRequest, Long adminUserId) {
        log.info("Adding question for subjectId: {}", addQuestionRequest.getSubjectId());

        AdminUser adminUser = adminUserDao.findById(adminUserId)
                .orElseThrow(() -> new AdminUserNotFoundException(String.format("Admin user not found with id [%d]", adminUserId)));

        Subject subject = subjectDao.findById(addQuestionRequest.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException(String.format("Subject not found with id [%d]", addQuestionRequest.getSubjectId())));

        Question question = new Question();
        question.setText(addQuestionRequest.getText());
        question.setOptions(List.of(
                addQuestionRequest.getOptionA(),
                addQuestionRequest.getOptionB(),
                addQuestionRequest.getOptionC(),
                addQuestionRequest.getOptionD()
        ));
        question.setCorrectAnswer(addQuestionRequest.getCorrectAnswer());
        question.setComplexity(addQuestionRequest.getComplexity());
        question.setMarks(addQuestionRequest.getMarks());
        question.setIsActive(Boolean.TRUE);
        question.setSubject(subject);
        question.setAssociatedExams(List.of());
        question.setParticipantUserExamQuestionAnswers(List.of());
        question.setCreatedByBaseUser(adminUser);
        question.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC));
        question.setCreationZone(adminUser.getZoneId());

        questionDao.save(question);
        log.debug("Question added with id: {}", question.getId());
    }

    @Transactional(readOnly = true)
    public QuestionDto getQuestionDtoById(Long id) {
        Question question = questionDao.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(String.format("Question not found with id: %d", id)));
        log.debug("Fetched question from DB with id [{}]", id);

        return QuestionDto.builder()
                .id(question.getId())
                .text(question.getText())
                .options(question.getOptions())
                .correctAnswer(question.getCorrectAnswer())
                .complexity(question.getComplexity())
                .marks(question.getMarks())
                .subjectId(question.getSubject().getId())
                .build();
    }

    @Transactional
    public void adminUpdateQuestion(UpdateQuestionRequestDto updateQuestionRequest) {
        Question question = questionDao.findById(updateQuestionRequest.getId())
                .orElseThrow(() -> new QuestionNotFoundException(String.format("Question not found with id [%d]", updateQuestionRequest.getId())));

        Subject subject = subjectDao.findById(updateQuestionRequest.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException(String.format("Subject not found with id [%d]", updateQuestionRequest.getSubjectId())));

        question.setText(updateQuestionRequest.getText());
        question.setOptions(new ArrayList<>(List.of(
                updateQuestionRequest.getOptionA(),
                updateQuestionRequest.getOptionB(),
                updateQuestionRequest.getOptionC(),
                updateQuestionRequest.getOptionD()
        )));
        question.setCorrectAnswer(updateQuestionRequest.getCorrectAnswer());
        question.setComplexity(updateQuestionRequest.getComplexity());
        question.setMarks(updateQuestionRequest.getMarks());
        question.setSubject(subject);

        questionDao.update(question);
    }

    @Transactional
    public void activateQuestion(Long questionId) {
        Question question = questionDao.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(String.format("Question not found with id [%d]", questionId)));

        question.setIsActive(Boolean.TRUE);

        questionDao.update(question);
        log.info("Successfully activated question with id [{}]", questionId);
    }

    @Transactional
    public void deactivateQuestion(Long questionId) {
        Question question = questionDao.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(String.format("Question not found with id [%d]", questionId)));

        question.setIsActive(Boolean.FALSE);

        questionDao.update(question);
        log.info("Successfully deactivated question with id [{}]", questionId);
    }

//    @Transactional(readOnly = true)
//    public QuestionPageViewDto getFilteredQuestionsForInstructor(AllQuestionFilterDto allQuestionFilterDto, Long instructorId) {
//        log.info("Invoked getFilteredQuestionsForInstructor with initial filter: {}", allQuestionFilterDto);
//
//        InstructorUser instructorUser = instructorUserDao.findById(instructorId)
//                .orElseThrow(() -> new InstructorUserNotFoundException(String.format("Instructor user not found with id: %d", instructorId)));
//
//        boolean hasAccessToSubject = instructorUser.getAssignedSubjects().stream()
//                .anyMatch(subject -> subject.getId().equals(allQuestionFilterDto.getSubjectId()));
//
//        if (!hasAccessToSubject) {
//            allQuestionFilterDto.setSubjectId(0L);
//        }
//
//        normalizeFilterDto(allQuestionFilterDto);
//
//        QuestionPageViewDto questionViewPageDto = questionDao.findFilteredInstructorCreatedQuestions(allQuestionFilterDto, instructorId);
//        log.info("Retrieved {} questions with applied filters", questionViewPageDto.getQuestions().size());
//
//        return questionViewPageDto;
//    }

//    @Transactional(readOnly = true)
//    public void isQuestionCreatedByInstructor(Long questionId, Long instructorId) {
//        Question question = questionDao.findById(questionId).orElseThrow(() -> new QuestionNotFoundException(String.format("Question not found with id: %d", questionId)));
//
//        instructorUserDao.findById(instructorId).orElseThrow(() -> new InstructorUserNotFoundException(String.format("Instructor user not found with id: %d", instructorId)));
//
//        boolean hasCreated = question.getCreatedByBaseUser().getId() == instructorId;
//
//        if (hasCreated) {
//            throw new AccessDeniedException(String.format("Instructor %d is not created question %d", instructorId, questionId));
//        }
//    }

//    @Transactional(readOnly = true)
//    public List<QuestionResponseDto> getAllInDto() {
//        return questionDao.findAll()
//                .stream()
//                .map(question -> QuestionResponseDto.builder()
//                        .id(question.getId())
//                        .text(question.getText())
//                        .options(question.getOptions())
//                        .correctAnswer(question.getCorrectAnswer())
//                        .complexity(question.getComplexity())
//                        .build())
//                .collect(Collectors.toList());
//    }
//
//    @Transactional
//    public void addAll(List<AddQuestionRequestDto> addQuestionRequestDtoList) {
//        addQuestionRequestDtoList.stream()
//                .forEach(question -> {
//                    Subject subject = subjectDao.findById(question.getSubjectId())
//                            .orElseThrow(() -> new EntityNotFoundException("Subject Not Found"));
//
//                    Question newQuestion = Question.builder()
//                            .text(question.getText())
//                            .correctAnswer(question.getCorrectAnswer())
//                            .complexity(question.getComplexity())
//                            .subject(subject)
//                            .build();
//                    newQuestion.getOptions().add(question.getOptionA());
//                    newQuestion.getOptions().add(question.getOptionB());
//                    newQuestion.getOptions().add(question.getOptionC());
//                    newQuestion.getOptions().add(question.getOptionD());
//
//                    subject.getQuestions().add(newQuestion);
//
//                    subjectDao.update(subject);
//                    questionDao.save(newQuestion);
//                });
//    }

    private void normalizeFilterDto(AllQuestionFilterDto allQuestionFilterDto) {
        if (allQuestionFilterDto.getSubjectId() == null || allQuestionFilterDto.getSubjectId() <= 0) {
            log.debug("Invalid or missing subjectId. Defaulting to 0");
            allQuestionFilterDto.setSubjectId(0L);
        }
        if (allQuestionFilterDto.getComplexity() == null || allQuestionFilterDto.getComplexity().isBlank()) {
            log.debug("Missing complexity. Defaulting to 'ALL'");
            allQuestionFilterDto.setComplexity("ALL");
        }
        if (allQuestionFilterDto.getMarks() == null || allQuestionFilterDto.getMarks() <= 0) {
            log.debug("Invalid or missing marks. Defaulting to 0");
            allQuestionFilterDto.setMarks(0L);
        }
        if (allQuestionFilterDto.getStatusFilter() == null || allQuestionFilterDto.getStatusFilter().isBlank()) {
            log.debug("Missing statusFilter. Defaulting to 'ALL'");
            allQuestionFilterDto.setStatusFilter("ALL");
        }
        if (allQuestionFilterDto.getSortBy() == null || allQuestionFilterDto.getSortBy().isBlank()) {
            log.debug("Missing sortBy. Defaulting to 'id'");
            allQuestionFilterDto.setSortBy("id");
        }
        if (allQuestionFilterDto.getSortOrder() == null || allQuestionFilterDto.getSortOrder().isBlank()) {
            log.debug("Missing sortOrder. Defaulting to 'ASC'");
            allQuestionFilterDto.setSortOrder("ASC");
        }
        if (allQuestionFilterDto.getPageSize() == null || allQuestionFilterDto.getPageSize() <= 0) {
            log.debug("Missing or invalid pageSize. Defaulting to 10");
            allQuestionFilterDto.setPageSize(10);
        }
        if (allQuestionFilterDto.getPageSize() != 5 && allQuestionFilterDto.getPageSize() != 10 && allQuestionFilterDto.getPageSize() != 20) {
            log.warn("Unsupported pageSize: {}. Resetting to default (10)", allQuestionFilterDto.getPageSize());
            allQuestionFilterDto.setPageSize(10);
        }
        if (allQuestionFilterDto.getPage() == null || allQuestionFilterDto.getPage() < 0) {
            log.debug("Missing or invalid page number. Defaulting to 0");
            allQuestionFilterDto.setPage(0);
        }

        log.info("Final applied filters - subjectId [{}], complexity [{}], marks [{}], statusFilter [{}], sortBy [{}], sortOrder [{}], pageSize [{}], page [{}]",
                allQuestionFilterDto.getSubjectId(),
                allQuestionFilterDto.getComplexity(),
                allQuestionFilterDto.getMarks(),
                allQuestionFilterDto.getStatusFilter(),
                allQuestionFilterDto.getSortBy(),
                allQuestionFilterDto.getSortOrder(),
                allQuestionFilterDto.getPageSize(),
                allQuestionFilterDto.getPage());
    }

    private void normalizeFilterDto(InstructorCreatedQuestionsFilterDto instructorCreatedQuestionsFilterDto) {
        if (instructorCreatedQuestionsFilterDto.getSubjectId() == null || instructorCreatedQuestionsFilterDto.getSubjectId() <= 0) {
            log.debug("Invalid or missing subjectId. Defaulting to 0");
            instructorCreatedQuestionsFilterDto.setSubjectId(0L);
        }
        if (instructorCreatedQuestionsFilterDto.getComplexity() == null || instructorCreatedQuestionsFilterDto.getComplexity().isBlank()) {
            log.debug("Missing complexity. Defaulting to 'ALL'");
            instructorCreatedQuestionsFilterDto.setComplexity("ALL");
        }
        if (instructorCreatedQuestionsFilterDto.getMarks() == null || instructorCreatedQuestionsFilterDto.getMarks() <= 0) {
            log.debug("Invalid or missing marks. Defaulting to 0");
            instructorCreatedQuestionsFilterDto.setMarks(0L);
        }
        if (instructorCreatedQuestionsFilterDto.getStatusFilter() == null || instructorCreatedQuestionsFilterDto.getStatusFilter().isBlank()) {
            log.debug("Missing statusFilter. Defaulting to 'ALL'");
            instructorCreatedQuestionsFilterDto.setStatusFilter("ALL");
        }
        if (instructorCreatedQuestionsFilterDto.getSortBy() == null || instructorCreatedQuestionsFilterDto.getSortBy().isBlank()) {
            log.debug("Missing sortBy. Defaulting to 'id'");
            instructorCreatedQuestionsFilterDto.setSortBy("id");
        }
        if (instructorCreatedQuestionsFilterDto.getSortOrder() == null || instructorCreatedQuestionsFilterDto.getSortOrder().isBlank()) {
            log.debug("Missing sortOrder. Defaulting to 'ASC'");
            instructorCreatedQuestionsFilterDto.setSortOrder("ASC");
        }
        if (instructorCreatedQuestionsFilterDto.getPageSize() == null || instructorCreatedQuestionsFilterDto.getPageSize() <= 0) {
            log.debug("Missing or invalid pageSize. Defaulting to 10");
            instructorCreatedQuestionsFilterDto.setPageSize(10);
        }
        if (instructorCreatedQuestionsFilterDto.getPageSize() != 5 && instructorCreatedQuestionsFilterDto.getPageSize() != 10 && instructorCreatedQuestionsFilterDto.getPageSize() != 20) {
            log.warn("Unsupported pageSize: {}. Resetting to default (10)", instructorCreatedQuestionsFilterDto.getPageSize());
            instructorCreatedQuestionsFilterDto.setPageSize(10);
        }
        if (instructorCreatedQuestionsFilterDto.getPage() == null || instructorCreatedQuestionsFilterDto.getPage() < 0) {
            log.debug("Missing or invalid page number. Defaulting to 0");
            instructorCreatedQuestionsFilterDto.setPage(0);
        }

        log.info("Final applied filters - subjectId [{}], complexity [{}], marks [{}], statusFilter [{}], sortBy [{}], sortOrder [{}], pageSize [{}], page [{}]",
                instructorCreatedQuestionsFilterDto.getSubjectId(),
                instructorCreatedQuestionsFilterDto.getComplexity(),
                instructorCreatedQuestionsFilterDto.getMarks(),
                instructorCreatedQuestionsFilterDto.getStatusFilter(),
                instructorCreatedQuestionsFilterDto.getSortBy(),
                instructorCreatedQuestionsFilterDto.getSortOrder(),
                instructorCreatedQuestionsFilterDto.getPageSize(),
                instructorCreatedQuestionsFilterDto.getPage());
    }

}
