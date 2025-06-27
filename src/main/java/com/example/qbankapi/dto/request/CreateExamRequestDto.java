package com.example.qbankapi.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CreateExamRequestDto {

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Subject is required")
    private Long subjectId;

    @NotNull @Min(value = 0, message = "Must be 0 or more")
    private Integer total1MarkQuestions;

    @NotNull @Min(value = 0, message = "Must be 0 or more")
    private Integer total2MarkQuestions;

    @NotNull @Min(value = 0, message = "Must be 0 or more")
    private Integer total3MarkQuestions;

    @NotNull @Min(value = 0, message = "Must be 0 or more")
    private Integer total4MarkQuestions;

    @NotNull @Min(value = 0, message = "Must be 0 or more")
    private Integer total5MarkQuestions;

    @NotNull @Min(value = 0, message = "Must be 0 or more")
    private Integer total6MarkQuestions;

}
