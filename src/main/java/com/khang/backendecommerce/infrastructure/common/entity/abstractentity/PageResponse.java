package com.khang.backendecommerce.infrastructure.common.entity.abstractentity;

import lombok.Builder;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Builder
public class PageResponse<T> {
    private Integer page;
    private Integer size;
    private Long total;
    private List<T> items;

    public static <T> PageResponse<T> of(List<T> items, Pageable pageable, long total) {
        return PageResponse.<T>builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .total(total)
                .items(items)
                .build();
    }
}
