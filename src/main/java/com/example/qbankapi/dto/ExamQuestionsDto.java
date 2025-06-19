package com.example.qbankapi.dto;

import com.example.qbankapi.entity.Question;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamQuestionsDto {

    private Long id;

    private String text;

    private List<String> options;

    private String correctAnswer;

    private Question.Complexity complexity;

}
