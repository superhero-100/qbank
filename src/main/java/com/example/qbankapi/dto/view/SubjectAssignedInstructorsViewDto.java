package com.example.qbankapi.dto.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectAssignedInstructorsViewDto {

    private SubjectViewDto subject;

    private List<InstructorUserViewDto> instructorUsers;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InstructorUserViewDto {

        private Long id;

        private String username;

        private String email;

        private String zoneId;

        private ZonedDateTime registeredAt;

        private String registerationZone;

    }

}
