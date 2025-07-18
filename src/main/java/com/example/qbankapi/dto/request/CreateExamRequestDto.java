package com.example.qbankapi.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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

//    @Future(message = "Enrollment Start Date Must be in future ")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "Enrollment start date is required")
    private LocalDateTime enrollmentStartDate;

//    @Future(message = "Enrollment end Date Must be in future ")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "Enrollment end date is required")
    private LocalDateTime enrollmentEndDate;

//    @Future(message = "Exam start Date Must be in future ")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "Exam start date is required")
    private LocalDateTime examStartDate;

//    @Future(message = "Exam end Date Must be in future ")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "Exam end date is required")
    private LocalDateTime examEndDate;

}
