package com.example.qbankapi.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamViewPageDto {

    private List<ExamDetailsDto> exams;

    private Integer page;

    private Integer pageSize;

    private Boolean hasNextPage;

}
