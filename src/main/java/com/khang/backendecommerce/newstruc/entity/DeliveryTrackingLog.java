package com.khang.backendecommerce.newstruc.entity;

import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="tbl_delivery_tracking_log")
public class DeliveryTrackingLog extends AbstractEntity<String> implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private DeliveryEntity delivery;


    @Column(name = "tracking_code")
    private String trackingCode;

    @Column(name = "message")
    private String message;

    @Column(name = "location")
    private String location ;


    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "old_status", columnDefinition = "order_status")
    private OrderStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "new_status", columnDefinition = "order_status")
    private OrderStatus newStatus;

    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name = "receiver_address")
    private String receiverAddress;

    @Column(name = "receiver_phone")
    private String receiverPhone;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;
//TODO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_order_id")
    private SubOrderEntity subOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;
}
