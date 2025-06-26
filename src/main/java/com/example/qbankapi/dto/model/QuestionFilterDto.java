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

    @NotNull(message = "please select subject")
    @Min(value = 1, message = "Invalid subject id")
    private Long subjectId = 0L;

    @NotBlank(message = "please select difficulty")
    @Pattern(
            regexp = "|EASY|MEDIUM|HARD",
            message = "Difficulty must be EASY, MEDIUM, or HARD"
    )
    private String difficulty = "";

    @NotNull(message = "please select marks")
    @Min(value = 1, message = "Marks must be 0 or more")
    @Max(value = 6, message = "Marks must be 0 or more")
    private Integer marks = 0;

    @NotBlank(message = "please write regx   otherwise use *")
    @Size(max = 100, message = "Question regex must be at most 100 characters")
    private String questionRegex = "";

    @NotBlank(message = "please write regx   otherwise use *")
    @Size(max = 100, message = "Option regex must be at most 100 characters")
    private String optionRegex = "";

    @NotBlank(message = "please select    sort by")
    @Pattern(
            regexp = "|id|marks|difficulty|subject",
            message = "SortBy must be one of: id, marks, difficulty, subject"
    )
    private String sortBy = "";

    @NotNull(message = "input page size default 10")
    @Min(value = 1, message = "Page size must be at least 1")
    private Integer pageSize = 10;

    @NotNull(message = "please enter page")
    @Min(value = 0, message = "Page number cannot be negative")
    private Integer page = 0;

}
