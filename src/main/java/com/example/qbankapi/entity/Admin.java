package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "admin_tbl")
@DiscriminatorValue("ADMIN")
public class Admin extends BaseUser { }
