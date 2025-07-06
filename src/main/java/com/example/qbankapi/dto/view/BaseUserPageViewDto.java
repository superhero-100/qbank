package com.example.qbankapi.dto.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseUserPageViewDto {

    private List<BaseUserViewDto> baseUsers;
    private Integer page;
    private Integer pageSize;
    private Boolean hasNextPage;

}
