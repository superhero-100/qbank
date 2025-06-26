package com.example.qbankapi.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddUserRequestDto {

    private String username;

    private String password;

}