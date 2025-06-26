package com.example.qbankapi.dto.request;

import com.example.qbankapi.entity.Question;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddQuestionRequestDto {

    private String text;

    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    private Question.Option correctAnswer;

    private Question.Complexity complexity;

    private Long marks;

    private Long subjectId;

}