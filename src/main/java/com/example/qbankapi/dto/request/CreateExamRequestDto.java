package com.example.qbankapi.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

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

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "Enrollment start date is required")
    private LocalDateTime enrollmentStartDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "Enrollment end date is required")
    private LocalDateTime enrollmentEndDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "Exam start date is required")
    private LocalDateTime examStartDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "Exam end date is required")
    private LocalDateTime examEndDate;

}
