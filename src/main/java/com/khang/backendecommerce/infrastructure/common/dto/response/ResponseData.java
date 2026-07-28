package com.khang.backendecommerce.infrastructure.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
public class ResponseData<T> implements Serializable {
    private final int status ;
    private final String message;
    private T data ;
}
