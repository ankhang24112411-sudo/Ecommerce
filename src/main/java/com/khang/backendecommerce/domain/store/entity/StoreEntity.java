package com.khang.backendecommerce.store.entity;

import com.khang.backendecommerce.catalog.inventory.entity.InventoryEntity;
import com.khang.backendecommerce.common.entity.abstractentity.AbstractEntity;
import com.khang.backendecommerce.location.entity.StateEntity;
import com.khang.backendecommerce.user.dto.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="tbl_store")
public class StoreEntity extends AbstractEntity<String> implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private UserEntity ownerId ;

    @Column(name = "store_name")
    private String name ;

    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    private StateEntity stateId ;
}
