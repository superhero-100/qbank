//package com.example.qbankapi.dto.model;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.validation.constraints.*;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class QuestionFilterDto {
//
//    @Min(value = 0, message = "Invalid subject id")
//    private Long subjectId;
//
//    @Pattern(
//            regexp = "ALL|EASY|MEDIUM|HARD",
//            message = "Complexity must be EASY, MEDIUM, or HARD"
//    )
//    private String complexity;
//
//    @Min(value = 0, message = "Marks must be at least 0")
//    @Max(value = 6, message = "Marks must not be more than 6")
//    private Long marks;
//
//    @Pattern(
//            regexp = "id|marks|complexity|subject",
//            message = "SortBy must be one of: id, marks, complexity, subject"
//    )
//    private String sortBy;
//
//    @Pattern(
//            regexp = "ASC|DESC",
//            message = "SortOrder must be one of: ASC, DESC"
//    )
//    private String sortOrder;
//
//    @Min(value = 1, message = "Page size must be at least 1")
//    private Integer pageSize;
//
//    @Min(value = 0, message = "Page number cannot be negative")
//    private Integer page;
//
//}
