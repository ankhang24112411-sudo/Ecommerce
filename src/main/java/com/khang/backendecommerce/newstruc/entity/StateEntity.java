package com.khang.backendecommerce.newstruc.entity;

import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
@Getter
@Setter
@Entity
@SuperBuilder

@NoArgsConstructor
@AllArgsConstructor
@Table(name ="tbl_state")
public class StateEntity extends AbstractEntity<String> implements Serializable {
    private String name;
}
