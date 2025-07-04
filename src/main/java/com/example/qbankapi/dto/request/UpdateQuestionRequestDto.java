package com.example.qbankapi.dto.request;

import com.example.qbankapi.entity.Question;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class UpdateQuestionRequestDto {

    private Long id;

    @NotBlank(message = "Question text is required")
    private String text;

    @NotBlank(message = "Option A is required")
    private String optionA;

    @NotBlank(message = "Option B is required")
    private String optionB;

    @NotBlank(message = "Option C is required")
    private String optionC;

    @NotBlank(message = "Option D is required")
    private String optionD;

    @NotNull(message = "Correct answer must be selected")
    private Question.Option correctAnswer;

    @NotNull(message = "Complexity must be selected")
    private Question.Complexity complexity;

    @NotNull(message = "Marks is required")
    private Long marks;

    @NotNull(message = "Subject is required")
    private Long subjectId;

}
