package com.example.qbankapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddSubjectRequestDto {

    @NotNull
    @Pattern(regexp = "^[A-Z][a-z]{1,49}$", message = "Subject name must start with a capital letter followed by lowercase letters (2–50 characters)")
    private String name;

}
