package com.example.qbankapi.dto.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstructorUserProfileStatsViewDto {

    private Long id;

    private String username;

    private String email;

    private String zoneId;

    private ZonedDateTime registeredAt;

    private Integer totalCreatedExams;

    private Integer totalCreatedQuestions;

    private Integer totalAssignedSubjects;

    private List<AssignedSubjectViewDto> assignedSubjects;

    private List<RecentCreatedExamViewDto> recentExams;

    private List<RecentCreatedQuestionsViewDto> recentQuestions;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignedSubjectViewDto {

        private Long id;

        private String name;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentCreatedExamViewDto {

        private String description;

        private String subjectName;

        private Integer totalMarks;

        private ZonedDateTime createdAt;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentCreatedQuestionsViewDto {

        private String questionText;

        private String subjectName;

        private ZonedDateTime createdAt;

    }

}
