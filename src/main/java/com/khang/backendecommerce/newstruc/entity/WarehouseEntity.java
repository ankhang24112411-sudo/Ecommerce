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

@Table(name ="tbl_warehouse")
public class WarehouseEntity extends AbstractEntity<String> implements Serializable {
    @Column(name = "name")
    private String name ;

    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    private StateEntity state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreEntity storeId;


}
