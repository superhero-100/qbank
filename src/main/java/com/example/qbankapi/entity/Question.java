package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "question_tbl")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "question_options",
            joinColumns = @JoinColumn(name = "question_id", nullable = false)
    )
    @Column(name = "option_value", nullable = false)
    @ToString.Exclude
    private List<String> options;

    @Column(name = "correct_answer", nullable = false)
    private String correctAnswer;

    @Enumerated(EnumType.STRING)
    @Column(name = "complexity", nullable = false)
    private Complexity complexity;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
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
