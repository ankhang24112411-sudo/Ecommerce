package com.khang.backendecommerce.newstruc.dto.request;

import com.khang.backendecommerce.newstruc.entity.ProductEntity;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public record ProductImageRequest (


     String image,

     Integer primary,

     Integer displayOrder
){
}
