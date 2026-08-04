package com.khang.backendecommerce.newstruc.entity;

import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="tbl_sub_order")
public class SubOrderEntity extends AbstractEntity<String> implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreEntity store ;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "order_status", columnDefinition = "order_status")
    private OrderStatus orderStatus;

    @Column(name ="store_name")
    private String storeName;

    @Column(name ="sub_total")
    private BigDecimal subTotal;

    @Column(name ="confirmed_at")
    private Instant confirmedAt;

    @Column(name ="rejected_at")
    private Instant rejectedAt;

    @Column(name ="rejection_reason")
    private String rejectionReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "delivery_route_id")
    private DeliveryRouteEntity deliveryRoute;

    @Column(name ="delivery_fee")
    private BigDecimal deliveryFee;

    @OneToMany(mappedBy = "subOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItems(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setSubOrder(this);
    }
}
