package com.example.qbankapi.dto.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantUserProfileStatsViewDto {

    private String username;

    private Integer totalExamsTaken;

    private Double averageScore;

    private Integer totalQuestionsAttempted;

    private Integer totalCorrectAnswers;

    private Double averageAccuracy;

    private List<RecentExamViewDto> recentExams;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentExamViewDto {

        private String examDescription;

        private String subjectName;

        private Integer score;

        private Date submittedAt;

        private Double accuracy;

    }

}
