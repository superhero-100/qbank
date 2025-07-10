package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "instructor_user_tbl")
@DiscriminatorValue("INSTRUCTOR")
@EqualsAndHashCode(callSuper = true)
public class InstructorUser extends BaseUser {

    @Column(name = "zone_id")
    private String zoneId;

    @ManyToMany
    @JoinTable(
            name = "instructor_user_assigned_subject_tbl",
            joinColumns = @JoinColumn(name = "instructor_user_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    @ToString.Exclude
    private List<Subject> assignedSubjects;

    @OneToMany(mappedBy = "createdByBaseUser")
    @ToString.Exclude
    private List<Question> createdQuestions;

    @OneToMany(mappedBy = "createdByBaseUser")
    @ToString.Exclude
    private List<Exam> createdExams;

}
