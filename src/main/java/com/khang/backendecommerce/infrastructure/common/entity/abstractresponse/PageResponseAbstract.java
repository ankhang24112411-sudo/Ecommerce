package com.khang.backendecommerce.infrastructure.common.entity.abstractresponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseAbstract {
    private int pageNumber;
    private int pageSize;
    private long totalPages;
    private long totalElements;
}
