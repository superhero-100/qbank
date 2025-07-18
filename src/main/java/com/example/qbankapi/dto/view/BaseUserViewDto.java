package com.example.qbankapi.dto.view;


import com.example.qbankapi.entity.BaseUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseUserViewDto {

    private Long id;
    private String username;
    private String email;
    private BaseUser.Role role;
    private BaseUser.Status status;

}
