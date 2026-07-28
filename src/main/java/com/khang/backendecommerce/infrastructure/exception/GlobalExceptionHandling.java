package com.khang.backendecommerce.infrastructure.exception;

import com.khang.backendecommerce.infrastructure.common.entity.abstractresponse.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandling {
    @ExceptionHandler({ConstraintViolationException.class,
            MissingServletRequestParameterException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(BAD_REQUEST)
   public ErrorResponse handleValidationException(Exception e , WebRequest request ){
       ErrorResponse errorResponse = ErrorResponse.builder()
               .timestamp(new Date())
               .status(BAD_REQUEST.value())
               .path(request.getDescription(false).replace("uri=", ""))
               .build();
       String message = e.getMessage();
       if(e instanceof MethodArgumentNotValidException){
           int start = message.lastIndexOf("[") + 1;
           int end = message.lastIndexOf("]") -1 ;
           message = message.substring(start, end);
           errorResponse.setMessage(message);
       }else if (e instanceof MissingServletRequestParameterException){
           errorResponse.setError("Invalid Parameter");
           errorResponse.setMessage(message);
       }else {
           errorResponse.setError("Invalid Data");
           errorResponse.setMessage(message);
       }
       return errorResponse;
   }
   public ErrorResponse handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e , WebRequest request){
       ErrorResponse errorResponse = ErrorResponse.builder()
               .timestamp(new Date())
               .status(UNAUTHORIZED.value())
               .path(request.getDescription(false).replace("uri=", ""))
               .error(UNAUTHORIZED.getReasonPhrase())
               .message("Username or Password is incorrect")
               .build();
       return errorResponse;
   }
    public ErrorResponse handleAccessDeniedException(Exception e , WebRequest request){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .status(FORBIDDEN.value())
                .path(request.getDescription(false).replace("uri=", ""))
                .error(FORBIDDEN.getReasonPhrase())
                .message(e.getMessage())
                .build();
        return errorResponse;
    }
    @ExceptionHandler(RessourceNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(Exception e , WebRequest request){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .status(FORBIDDEN.value())
                .path(request.getDescription(false).replace("uri=", ""))
                .error(FORBIDDEN.getReasonPhrase())
                .message(e.getMessage())
                .build();
        return errorResponse;
    }
    @ExceptionHandler(InvalidDataException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse handleDuplicateKeyException(InvalidDataException e, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .status(CONFLICT.value())
                .path(request.getDescription(false).replace("uri=", ""))
                .error(CONFLICT.getReasonPhrase())
                .message(e.getMessage())
                .build();
        return errorResponse;
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .status(INTERNAL_SERVER_ERROR.value())
                .path(request.getDescription(false).replace("uri=", ""))
                .error(INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(e.getMessage())
                .build();

        return errorResponse;
    }







}
