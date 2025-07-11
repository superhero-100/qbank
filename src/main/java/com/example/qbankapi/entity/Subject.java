package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "subject_tbl")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Subject extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "subject")
    @ToString.Exclude
    private List<Question> questions;

    @OneToMany(mappedBy = "subject")
    @ToString.Exclude
    private List<Exam> exams;

    @ManyToMany(mappedBy = "assignedSubjects")
    @ToString.Exclude
    private List<InstructorUser> assignedInstructors;

}