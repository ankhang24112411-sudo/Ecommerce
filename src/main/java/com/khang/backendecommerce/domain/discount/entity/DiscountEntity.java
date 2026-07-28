package com.khang.backendecommerce.domain.discount.entity;

import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import com.khang.backendecommerce.infrastructure.common.enums.DiscountStatus;
import com.khang.backendecommerce.infrastructure.common.enums.DiscountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="tbl_discount")
public class DiscountEntity extends AbstractEntity<String> implements Serializable {
    @Column(name ="discount_name")
    private Integer discountName;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "discount_type", columnDefinition = "discount_type" )
    private DiscountType discountType;

    @Column(name ="valid_from")
    private Instant valid_from;

    @Column(name ="valid_to")
    private Instant valid_to;

    @Column(name ="discount_value")
    private BigDecimal discountValue = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "discount_status", columnDefinition = "discount_status" )
    private DiscountStatus discountStatus;

    @Column(name ="description")
    private String description;



}
