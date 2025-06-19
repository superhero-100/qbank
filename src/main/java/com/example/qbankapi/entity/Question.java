package com.example.qbankapi.entity;

import com.example.qbankapi.dao.SubjectDao;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "question_tbl")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "question_options",
            joinColumns = @JoinColumn(name = "question_id")
    )
    @Column(name = "option_value")
    @Builder.Default
    @ToString.Exclude
    private List<String> options = new ArrayList<>();

    @Column(name = "correct_answer")
    private String correctAnswer;

    @Enumerated(EnumType.STRING)
    private Complexity complexity;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    public enum Complexity {
        EASY(1), MEDIUM(3), HARD(5);

        private final int marks;

        Complexity(int marks) {
            this.marks = marks;
        }

        public int getMarks() {
            return marks;
        }
    }

}
