package com.example.qbankapi.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "instructor_user_tbl")
@DiscriminatorValue("INSTRUCTOR")
public class InstructorUser extends BaseUser {

    @Column(name = "zone_id")
    private String zoneId;

}
