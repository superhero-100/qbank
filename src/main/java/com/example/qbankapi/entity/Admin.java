package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admin_tbl")
@DiscriminatorValue("ADMIN")
public class Admin extends BaseUser{}
