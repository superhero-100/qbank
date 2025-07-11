package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@MappedSuperclass
public abstract class Auditable {

    @Column(name = "created_at", updatable = false, nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "creation_zone", updatable = false, nullable = false)
    private String creationZone;

}
