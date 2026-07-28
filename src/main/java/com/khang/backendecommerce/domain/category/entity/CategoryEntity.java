package com.khang.backendecommerce.domain.category.entity;

import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name ="tbl_category")
public class CategoryEntity extends AbstractEntity<String> implements Serializable {
    @Column(name = "category_name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "picture")
    private String picture;
}
