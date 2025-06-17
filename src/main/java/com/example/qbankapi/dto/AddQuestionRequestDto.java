package com.example.qbankapi.dto;

import com.example.qbankapi.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddQuestionRequestDto {

    @NotBlank(message = "Question text must not be blank")
    @Size(min = 5, max = 500, message = "Question text must be between 5 and 500 characters")
    private String text;

    @NotBlank(message = "Option A must not be blank")
    private String optionA;

    @NotBlank(message = "Option B must not be blank")
    private String optionB;

    @NotBlank(message = "Option C must not be blank")
    private String optionC;

    @NotBlank(message = "Option D must not be blank")
    private String optionD;

    @NotBlank(message = "Correct answer must not be blank")
    @Pattern(regexp = "A|B|C|D", message = "Correct answer must be one of: A, B, C, D")
    private String correctAnswer;

    @NotNull(message = "Complexity must not be null")
    private Question.Complexity complexity;

    @NotNull(message = "Subject ID must not be null")
    @Positive(message = "Subject ID must be a positive number")
    private Long subjectId;

}