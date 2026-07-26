package com.khang.backendecommerce.domain.delivery.entity;

import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import com.khang.backendecommerce.domain.location.entity.StateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="tbl_delivery_route")
public class DeliveryRoute extends AbstractEntity<String> implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_from_id")
    private StateEntity stateFromId ;

    @Column(name = "state_from_name")
    private String stateFrom ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_to_id")
    private String stateToId ;

    @Column(name = "state_to_name")
    private String stateTo ;


}
