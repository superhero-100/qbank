package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "admin_user_tbl")
@DiscriminatorValue("ADMIN")
public class AdminUser extends BaseUser {

    @Column(name = "zone_id")
    private String zoneId;

}
