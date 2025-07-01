package com.example.qbankapi.dto.model;

import com.example.qbankapi.entity.BaseUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseUserViewPageDto {

    private List<BaseUserDetailsDto> baseUsers;
    private Integer page;
    private Integer pageSize;
    private Boolean hasNextPage;

}
