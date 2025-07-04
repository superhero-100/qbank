package com.example.qbankapi.dto.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionPageViewDto {

    private List<QuestionViewDto> questions;

    private Integer page;

    private Integer pageSize;

    private Boolean hasNextPage;

}
