package com.khang.backendecommerce.newstruc.entity;

import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import com.khang.backendecommerce.infrastructure.common.enums.PaymentMethod;
import com.khang.backendecommerce.infrastructure.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name ="tbl_order")
public class OrderEntity extends AbstractEntity<String> implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private UserEntity customer ;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_discount_id")
//    private DiscountOrderEntity discountOrder ;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "order_code")
    private String orderCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    private StateEntity state ;

    @Column(name ="subtotal" , precision = 12 , scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name ="discount_quantity")
    private Integer discountQuantity;

    @Column(name ="discount_value" ,  precision = 12 , scale = 2)
    private BigDecimal discountValue = BigDecimal.ZERO;

    @Column(name ="discount_amount" ,  precision = 12 , scale = 2)
    private BigDecimal discountTotalAmount = BigDecimal.ZERO;

    @Column(name ="total_amount" ,  precision = 12 , scale = 2)
    private BigDecimal orderTotalAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "payment_method", columnDefinition = "payment_method" )
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "payment_status", columnDefinition = "payment_status" )
    private PaymentStatus paymentStatus;

    @Column(name = "delivered_at")
    private Instant deliveredAt;

    @Column(name = "confirmed_at")
    private Instant confirmedAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubOrderEntity> subOrders;

    public void addSubOrder(SubOrderEntity subOrder){
        subOrders.add(subOrder);
        subOrder.setOrder(this);
    }
}
