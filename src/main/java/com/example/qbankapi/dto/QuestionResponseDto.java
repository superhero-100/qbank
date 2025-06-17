package com.example.qbankapi.dto;

import com.example.qbankapi.entity.Question;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDto {

    private Long id;

    private String text;

    @Builder.Default
    private List<String> options = new ArrayList<>();

    private String correctAnswer;

    private Question.Complexity complexity;

}
