package com.example.qbankapi.dto.model;


import com.example.qbankapi.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateQuestionDto {

    private Long id;

    private String text;

    private List<String> options;

    private Question.Option correctAnswer;

    private Question.Complexity complexity;

    private Long marks;

    private Long subjectId;

}
