package com.example.qbankapi.dto.model;

import com.example.qbankapi.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamSubmissionDto {

    private Long examId;

    @Builder.Default
    private Map<Long, Question.Option> answers = new LinkedHashMap<>();

}
