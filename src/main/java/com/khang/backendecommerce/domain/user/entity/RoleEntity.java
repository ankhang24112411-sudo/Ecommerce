package com.khang.backendecommerce.domain.role;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_role")
public class RoleEntity extends AbstractEntity<String> implements Serializable {
    @Column(name = "name")
    private String name;
}
