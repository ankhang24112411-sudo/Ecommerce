package com.khang.backendecommerce.infrastructure.common.entity.abstractresponse;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse{
    private Date timestamp;
    private int status;
    private String path;
    private String error;
    private String message;

}