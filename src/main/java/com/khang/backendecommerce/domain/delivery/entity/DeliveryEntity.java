package com.khang.backendecommerce.domain.delivery.entity;

import com.khang.backendecommerce.domain.cart.entity.CartItemEntity;
import com.khang.backendecommerce.domain.order.entity.OrderEntity;
import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import com.khang.backendecommerce.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="tbl_delivery")
public class DeliveryEntity extends AbstractEntity<String> implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_shipper_id")
    private UserEntity shipperId ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity orderId ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private UserEntity customerId ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private DeliveryCompanyEntity companyId ;

    @Column(name = "tracking_code")
    private String trackingCode;

    @Column(name = "proof_image")
    private String proofImage;

    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name = "receiver_address")
    private String receiverAddress;

    @Column(name = "completed_at")
    private String completedAt;

    @OneToMany(mappedBy = "delivery")
    private List<DeliveryTrackingLog> deliveryTrackingLogList = new ArrayList<>();
}
