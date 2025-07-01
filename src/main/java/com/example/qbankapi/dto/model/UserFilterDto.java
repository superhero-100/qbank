package com.example.qbankapi.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFilterDto {

    @Pattern(
            regexp = "ALL|USER|ADMIN",
            message = "Role must be ALL, USER, or ADMIN"
    )
    private String role;

    @Pattern(
            regexp = "ALL|ACTIVE|INACTIVE|LOCKED",
            message = "Status must be ALL, ACTIVE, INACTIVE, or LOCKED"
    )
    private String status;

    private String usernameRegxPattern;

    @Pattern(
            regexp = "id|username|email|roleValue",
            message = "SortBy must be one of: id, username, email, or roleValue"
    )
    private String sortBy;

    @Pattern(
            regexp = "ASC|DESC",
            message = "SortOrder must be one of: ASC, or DESC"
    )
    private String sortOrder;

    @Min(value = 1, message = "Page size must be at least 1")
    private Integer pageSize;

    @Min(value = 0, message = "Page number cannot be negative")
    private Integer page;

}
