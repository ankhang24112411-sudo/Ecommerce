package com.khang.backendecommerce.domain.payment.entity;

import com.khang.backendecommerce.domain.order.entity.OrderEntity;
import com.khang.backendecommerce.domain.user.entity.UserEntity;
import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;
import com.khang.backendecommerce.infrastructure.common.enums.PaymentMethod;
import com.khang.backendecommerce.infrastructure.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="tbl_payment")
public class PaymentEntity  extends AbstractEntity<String> implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order ;

    @Column(name ="transaction_code")
    private String transactionCode;

    @Column(name ="paid_at")
    private Instant failedAt;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "payment_method", columnDefinition = "payment_method")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "payment_status", columnDefinition = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name ="refunded_amount")
    private Integer refundedAmount;

}
