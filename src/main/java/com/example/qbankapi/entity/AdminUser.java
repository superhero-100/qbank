package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "admin_user_tbl")
@DiscriminatorValue("ADMIN")
@EqualsAndHashCode(callSuper = true)
public class AdminUser extends BaseUser {

    @Column(name = "zone_id")
    private String zoneId;

    @OneToMany(mappedBy = "createdByBaseUser")
    @ToString.Exclude
    private List<Question> createdQuestions;

    @OneToMany(mappedBy = "createdByBaseUser")
    @ToString.Exclude
    private List<Exam> createdExams;

}
