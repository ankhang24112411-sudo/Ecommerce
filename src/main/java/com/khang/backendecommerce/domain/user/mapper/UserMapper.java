package com.khang.backendecommerce.domain.user.mapper;

import com.khang.backendecommerce.domain.user.dto.UserCreationRequest;
import com.khang.backendecommerce.domain.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.springframework.web.bind.annotation.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toUser(UserCreationRequest request);
//    @Mapping(source = "username", target = "username")
//    UserDetailResponse toUserDetailResponse( UserEntity user);
}
