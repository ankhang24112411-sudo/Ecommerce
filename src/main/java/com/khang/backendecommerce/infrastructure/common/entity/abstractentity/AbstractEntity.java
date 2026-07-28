package com.khang.backendecommerce.infrastructure.common.entity.abstractentity;

import com.khang.backendecommerce.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AbstractEntity<T> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "created_at", updatable = false , nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at",  nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "deleted" )
    private Short deleted;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;



}
