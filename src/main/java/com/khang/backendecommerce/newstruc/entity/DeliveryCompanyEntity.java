package com.khang.backendecommerce.newstruc.entity;

import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import com.khang.backendecommerce.infrastructure.common.enums.DeliveryCompanyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="tbl_delivery_company")
@SuperBuilder

public class DeliveryCompanyEntity extends AbstractEntity<String> implements Serializable {
    @Column(name = "name")
    private String name ;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "delivery_company_status", columnDefinition = "delivery_company_status" )
    private DeliveryCompanyStatus companyStatus;


}
