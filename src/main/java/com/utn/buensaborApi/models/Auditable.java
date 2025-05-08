package com.utn.buensaborApi.models;

import jakarta.persistence.*;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<U> {

    @CreatedBy
    @Column(updatable = false)
    private U createdBy;

    @LastModifiedBy
    private U updatedBy;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}

