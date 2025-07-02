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
@ToString
@Entity
@Table(name = "teacher_tbl")
@DiscriminatorValue("TEACHER")
public class Teacher extends BaseUser {

    @Column(name = "zone_id")
    private String zoneId;

}
