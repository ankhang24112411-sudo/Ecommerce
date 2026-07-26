package com.khang.backendecommerce.common.entity.abstractentity;

import com.khang.backendecommerce.user.dto.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CollectionIdJdbcTypeCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Temporal;

import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AbstractEntity<T> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private T id;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at", updatable = false)
    @CreationTimestamp
    private Instant updatedAt;

    @Column(name = "deleted" , updatable = false)
    private Short deleted;

    @OneToOne
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;

    @OneToOne
    @JoinColumn(name = "updated_by")
    private UserEntity updatedBy;

}
