package com.example.qbankapi.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionFilterDto {

    @Min(value = 0, message = "Invalid subject id")
    private Long subjectId;

    @Pattern(
            regexp = "ALL|EASY|MEDIUM|HARD",
            message = "Difficulty must be EASY, MEDIUM, or HARD"
    )
    private String difficulty;

    @Min(value = 0, message = "Marks must be at least 0")
    @Max(value = 6, message = "Marks must not be more than 6")
    private Integer marks;

    @Pattern(
            regexp = "id|marks|difficulty|subject",
            message = "SortBy must be one of: id, marks, difficulty, subject"
    )
    private String sortBy;

    @Min(value = 1, message = "Page size must be at least 1")
    private Integer pageSize;

    @Min(value = 0, message = "Page number cannot be negative")
    private Integer page;

    public void normalize() {
        if (subjectId == null) subjectId = 0L;
        if (difficulty == null || difficulty.isBlank()) difficulty = "ALL";
        if (marks == null) marks = 0;

        if (sortBy == null || sortBy.isBlank()) sortBy = "id";
        if (pageSize == null || pageSize <= 0) pageSize = 10;
        if (page == null || page < 0) page = 0;
    }
}
