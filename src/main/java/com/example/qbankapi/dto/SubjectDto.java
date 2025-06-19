package com.example.qbankapi.dto;

import com.example.qbankapi.entity.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDto {

    private Long id;

    private String name;

    private Integer totalAvailableQuestions;

    private Integer totalAvailableExams;

    public void save(Subject subject) {
    }
}
