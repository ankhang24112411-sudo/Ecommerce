package com.khang.backendecommerce.newstruc.entity;

import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder

@Table(name = "tbl_delivery_route")
public class DeliveryRouteEntity extends AbstractEntity<String> implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_from_id")
    private StateEntity stateFrom;

    @Column(name = "state_from_name")
    private String stateFromName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_to_id")
    private StateEntity stateTo;

    @Column(name = "state_to_name")
    private String stateToName;


}
