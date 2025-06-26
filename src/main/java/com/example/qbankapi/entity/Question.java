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

    @Column(name = "text")
    private String text;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "question_options",
            joinColumns = @JoinColumn(name = "question_id")
    )
    @Column(name = "option_value")
    @ToString.Exclude
    private List<String> options;

    @Column(name = "correct_answer")
    private Option correctAnswer;

    @Enumerated(EnumType.STRING)
    @Column(name = "complexity")
    private Complexity complexity;

    @Column(name = "marks")
    private Long marks;

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

    public enum Option {
        A,B,C,D
    }

}
