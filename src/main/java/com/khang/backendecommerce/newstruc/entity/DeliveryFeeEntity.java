package com.khang.backendecommerce.newstruc.entity;

import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="tbl_delivery_fee")
public class DeliveryFeeEntity extends AbstractEntity<String> implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private DeliveryCompanyEntity companyId ;

    @Column(name = "base_fee")
    private BigDecimal baseFee ;

    @Column(name = "delivery_time")
    private Integer deliveryTime ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_route_id")
    private DeliveryRouteEntity deliveryRoute ;

}
