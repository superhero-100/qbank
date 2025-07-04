package com.example.qbankapi.dto.view;

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
public class QuestionViewDto {

    private Long id;

    private String text;

    private List<String> options;

    private Question.Option correctAnswer;

    private Question.Complexity complexity;

    private Long marks;

    private String subjectName;

}
