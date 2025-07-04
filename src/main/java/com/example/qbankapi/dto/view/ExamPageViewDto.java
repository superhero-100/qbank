package com.example.qbankapi.dto.view;

import com.example.qbankapi.dto.view.ExamViewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamPageViewDto {

    private List<ExamViewDto> exams;

    private Integer page;

    private Integer pageSize;

    private Boolean hasNextPage;

}
