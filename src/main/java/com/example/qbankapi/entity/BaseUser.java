package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "base_user_tbl")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
public class BaseUser extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "username", unique = true)
    protected String username;

    @Column(name = "email", unique = true)
    protected String email;

    @Column(name = "password")
    protected String password;

    @Column(name = "role", insertable = false, updatable = false)
    protected String roleValue;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @Transient
    public Role getRole() {
        return Role.valueOf(roleValue);
    }

    public enum Role {
        ADMIN,
        USER
    }

    public enum Status {
        ACTIVE, INACTIVE, LOCKED
    }

}