package com.example.qbankapi.service;

import com.example.qbankapi.dao.BaseUserDao;
import com.example.qbankapi.dao.QuestionDao;
import com.example.qbankapi.dao.SubjectDao;
import com.example.qbankapi.dto.model.QuestionDto;
import com.example.qbankapi.dto.model.QuestionFilterDto;
import com.example.qbankapi.dto.request.AddQuestionRequestDto;
import com.example.qbankapi.dto.request.UpdateQuestionRequestDto;
import com.example.qbankapi.dto.view.QuestionAnalyticsViewDto;
import com.example.qbankapi.dto.view.QuestionPageViewDto;
import com.example.qbankapi.entity.BaseUser;
import com.example.qbankapi.entity.ParticipantUserExamQuestionAnswer;
import com.example.qbankapi.entity.Question;
import com.example.qbankapi.entity.Subject;
import com.example.qbankapi.exception.base.BaseUserNotFoundException;
import com.example.qbankapi.exception.base.impl.QuestionNotFoundException;
import com.example.qbankapi.exception.base.impl.SubjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionDao questionDao;
    private final SubjectDao subjectDao;
    private final BaseUserDao baseUserDao;

    @Transactional(readOnly = true)
    public QuestionPageViewDto getFilteredQuestions(QuestionFilterDto questionFilterDto) {
        log.info("Invoked getFilteredQuestions with initial filter: {}", questionFilterDto);

        if (questionFilterDto.getSubjectId() == null || questionFilterDto.getSubjectId() <= 0) {
            log.debug("Invalid or missing subjectId. Defaulting to 0");
            questionFilterDto.setSubjectId(0L);
        }
        if (questionFilterDto.getComplexity() == null || questionFilterDto.getComplexity().isBlank()) {
            log.debug("Missing complexity. Defaulting to 'ALL'");
            questionFilterDto.setComplexity("ALL");
        }
        if (questionFilterDto.getMarks() == null || questionFilterDto.getMarks() <= 0) {
            log.debug("Invalid or missing marks. Defaulting to 0");
            questionFilterDto.setMarks(0L);
        }
        if (questionFilterDto.getSortBy() == null || questionFilterDto.getSortBy().isBlank()) {
            log.debug("Missing sortBy. Defaulting to 'id'");
            questionFilterDto.setSortBy("id");
        }
        if (questionFilterDto.getStatusFilter() == null || questionFilterDto.getStatusFilter().isBlank()) {
            log.debug("Missing statusFilter. Defaulting to 'ALL'");
            questionFilterDto.setStatusFilter("ALL");
        }
        if (questionFilterDto.getSortOrder() == null || questionFilterDto.getSortOrder().isBlank()) {
            log.debug("Missing sortOrder. Defaulting to 'ASC'");
            questionFilterDto.setSortOrder("ASC");
        }
        if (questionFilterDto.getPageSize() == null || questionFilterDto.getPageSize() <= 0) {
            log.debug("Missing or invalid pageSize. Defaulting to 10");
            questionFilterDto.setPageSize(10);
        }
        if (questionFilterDto.getPageSize() != 5 && questionFilterDto.getPageSize() != 10 && questionFilterDto.getPageSize() != 20) {
            log.warn("Unsupported pageSize: {}. Resetting to default (10)", questionFilterDto.getPageSize());
            questionFilterDto.setPageSize(10);
        }
        if (questionFilterDto.getPage() == null || questionFilterDto.getPage() < 0) {
            log.debug("Missing or invalid page number. Defaulting to 0");
            questionFilterDto.setPage(0);
        }

        log.info("Final applied filters - subjectId: {}, complexity: {}, marks: {}, statusFilter: {}, sortBy: {}, sortOrder: {}, pageSize: {}, page: {}",
                questionFilterDto.getSubjectId(),
                questionFilterDto.getComplexity(),
                questionFilterDto.getMarks(),
                questionFilterDto.getStatusFilter(),
                questionFilterDto.getSortBy(),
                questionFilterDto.getSortOrder(),
                questionFilterDto.getPageSize(),
                questionFilterDto.getPage());

        QuestionPageViewDto questionViewPageDto = questionDao.findFilteredQuestions(questionFilterDto);
        log.info("Retrieved {} questions with applied filters", questionViewPageDto.getQuestions().size());

        return questionViewPageDto;
    }

    @Transactional
    public void addQuestion(AddQuestionRequestDto addQuestionRequest, Long baseUserId) {
        log.info("Adding question for subjectId: {}", addQuestionRequest.getSubjectId());

        BaseUser baseUser = baseUserDao.findById(baseUserId)
                .orElseThrow(() -> new BaseUserNotFoundException(String.format("Subject not found with id: %d", addQuestionRequest.getSubjectId())));

        Subject subject = subjectDao.findById(addQuestionRequest.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException(String.format("Subject not found with id: %d", addQuestionRequest.getSubjectId())));

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
        question.setCreatedByBaseUser(baseUser);
        question.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC));
        question.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));

        questionDao.save(question);
        log.debug("Question added with id: {}", question.getId());
    }

    @Transactional(readOnly = true)
    public QuestionDto getQuestionDtoById(Long id) {
        Question question = questionDao.findById(id).orElseThrow(() -> new QuestionNotFoundException(String.format("Question not found with id: %d", id)));
        log.debug("Question found with id: {}", id);
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
    public void updateQuestion(UpdateQuestionRequestDto updateQuestionRequest) {
        Question question = questionDao.findById(updateQuestionRequest.getId())
                .orElseThrow(() -> new QuestionNotFoundException("Question not found with id: " + updateQuestionRequest.getId()));

        Subject subject = subjectDao.findById(updateQuestionRequest.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException("Subject not found with id: " + updateQuestionRequest.getSubjectId()));

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
        question.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
        questionDao.update(question);
    }

    @Transactional(readOnly = true)
    public QuestionAnalyticsViewDto getQuestionAnalytics(Long questionId) {
        Question question = questionDao.findById(questionId).orElseThrow(() -> new QuestionNotFoundException(String.format("Question not found with id: %d", questionId)));

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
                .build();
    }

    @Transactional
    public void activateQuestion(Long questionId) {
        Question question = questionDao.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException("Question not found with id: " + questionId));
        question.setIsActive(Boolean.TRUE);
        question.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
        questionDao.update(question);
        log.info("Successfully activated question with ID: {}", questionId);
    }

    @Transactional
    public void deactivateQuestion(Long questionId) {
        Question question = questionDao.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException("Question not found with id: " + questionId));
        question.setIsActive(Boolean.FALSE);
        question.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
        questionDao.update(question);
        log.info("Successfully deactivated question with ID: {}", questionId);
    }

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

}
