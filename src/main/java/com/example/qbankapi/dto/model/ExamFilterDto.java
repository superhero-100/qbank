package com.example.qbankapi.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamFilterDto {

    @Min(value = 0, message = "Invalid subject id")
    private Long subjectId;

    @Pattern(
            regexp = "createdAt|totalMarks",
            message = "SortBy must be one of: created_at, marks"
    )
    private String sortBy;

    @Pattern(
            regexp = "ASC|DESC",
            message = "SortOrder must be one of: ASC, DESC"
    )
    private String sortOrder;

    @Min(value = 1, message = "Page size must be at least 1")
    private Integer pageSize;

    @Min(value = 0, message = "Page number cannot be negative")
    private Integer page;

}
