package com.khang.backendecommerce.api;

import com.khang.backendecommerce.newstruc.dto.request.UserCreationRequest;
import com.khang.backendecommerce.newstruc.service.UserService;
import com.khang.backendecommerce.infrastructure.common.dto.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Validated
@Slf4j
@Tag(name ="User Controller")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(method = "POST" , summary = "Add new user" )
    @PostMapping("/")
    public ResponseEntity<BaseResponse<String>> addUser(@RequestBody UserCreationRequest request){
        log.info("Request add user {} {}" , request.getUsername(), request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(userService.addUser(request) , "success"));
    }
}
