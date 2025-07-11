package com.example.qbankapi.dto.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantUserProfileStatsViewDto {

    private String username;

    private String email;

    private String zoneId;

    private ZonedDateTime registeredAt;

    private String registrationZone;

    private Integer totalExamsEnrolled;

    private Integer totalExamsTaken;

    private Integer bestScore;

    private Integer worstScore;

    private Double averageScore;

    private Integer totalQuestionsAttempted;

    private Integer totalCorrectAnswers;

    private Double averageAccuracy;

    private ZonedDateTime lastExamTakenAt;

    private String lastExamTakenZone;

    private List<RecentExamViewDto> recentExams;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentExamViewDto {

        private String examDescription;

        private String subjectName;

        private Integer score;

        private ZonedDateTime submittedAt;

        private String submissionZone;

        private Double accuracy;

    }

}
