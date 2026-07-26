package com.khang.backendecommerce.location.entity;

import com.khang.backendecommerce.common.entity.abstractentity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="tbl_state")
public class StateEntity extends AbstractEntity<String> implements Serializable {
    private String name;
}
