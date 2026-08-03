package com.khang.backendecommerce.newstruc.domain.user.mapper;

import com.khang.backendecommerce.newstruc.domain.user.dto.UserCreationRequest;
import com.khang.backendecommerce.newstruc.domain.user.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NewUserMapper {
    UserEntity toUser(UserCreationRequest request);
//    @Mapping(source = "username", target = "username")
//    UserDetailResponse toUserDetailResponse( UserEntity user);
}
