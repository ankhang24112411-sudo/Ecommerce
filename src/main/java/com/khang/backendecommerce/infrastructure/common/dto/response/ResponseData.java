package com.khang.backendecommerce.infrastructure.common.entity.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class ResponseData<T> implements Serializable {
    private final int status ;
    private final String message;
    private T data ;
}
