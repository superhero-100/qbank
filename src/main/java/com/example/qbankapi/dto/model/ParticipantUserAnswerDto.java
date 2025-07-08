package com.example.qbankapi.dto.model;

import com.example.qbankapi.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantUserAnswerDto {

    private Long questionId;

    private Question.Option answer;

}
