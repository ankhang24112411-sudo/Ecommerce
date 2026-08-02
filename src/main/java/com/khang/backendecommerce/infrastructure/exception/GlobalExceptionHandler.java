package com.khang.backendecommerce.infrastructure.exception;

import com.khang.backendecommerce.infrastructure.common.dto.response.BaseResponse;
import com.khang.backendecommerce.newstruc.domain.order.rules.config.CheckoutValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static io.lettuce.core.pubsub.PubSubOutput.Type.message;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<BaseResponse<Object>> handleRuntimeException(
            ApplicationException e
    ) {
        return ResponseEntity.status(e.getHttpStatus()).body(new BaseResponse<>(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BaseResponse<Object>> handleRuntimeException(
            RuntimeException e
    ) {
        log.error("Exception ", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse<>(500, null, "System error, plz try later"));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<BaseResponse<Object>> handleRuntimeException(
            AuthorizationDeniedException e
    ) {
        log.error("Exception ", e);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new BaseResponse<>(403, null, "User unauthorized"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>>
    handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        List<FieldError> fieldErrors =
                e.getBindingResult().getFieldErrors();

        List<String> errorMessages = new ArrayList<>();

        for (FieldError fieldError : fieldErrors) {
            String errorMessage = fieldError.getField() + ": " + fieldError.getDefaultMessage();
            errorMessages.add(errorMessage);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse<>(400, null, errorMessages.toString()));
    }
    @ExceptionHandler(CheckoutValidationException.class)
    public ResponseEntity<BaseResponse<Object>>
            handleMethodArgumentNotValidException(CheckoutValidationException exception){
        List<String> errorMessages = exception.getViolations().stream()
                .map(violation -> {
                    StringBuilder message = new StringBuilder(violation.error().getMessage());
                    if(violation.field()!= null){
                        message.append(" | field").append(violation.referenceId());
                    }
                    if(violation.referenceId() != null){
                        message.append(" | referenceId: ").append(violation.referenceId());
                    }
                    return message.toString();

                }).toList();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse<>(400, null, errorMessages.toString()));
    }
}