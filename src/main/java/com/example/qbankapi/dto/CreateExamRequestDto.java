package com.example.qbankapi.dto;

import lombok.*;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateExamRequestDto {

    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @NotNull(message = "Subject ID is required")
    @Positive(message = "Subject ID must be a positive number")
    private Long subjectId;

    @NotNull(message = "Total easy questions are required")
    @Min(value = 0, message = "Total easy questions must be zero or more")
    private Integer totalEasyQuestions;

    @NotNull(message = "Total medium questions are required")
    @Min(value = 0, message = "Total medium questions must be zero or more")
    private Integer totalMediumQuestions;

    @NotNull(message = "Total hard questions are required")
    @Min(value = 0, message = "Total hard questions must be zero or more")
    private Integer totalHardQuestions;

}
