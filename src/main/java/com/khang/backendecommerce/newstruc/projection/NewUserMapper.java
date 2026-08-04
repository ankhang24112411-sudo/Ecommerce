package com.khang.backendecommerce.newstruc.projection;

import com.khang.backendecommerce.newstruc.dto.request.UserCreationRequest;
import com.khang.backendecommerce.newstruc.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NewUserMapper {
    UserEntity toUser(UserCreationRequest request);
//    @Mapping(source = "username", target = "username")
//    UserDetailResponse toUserDetailResponse( UserEntity user);
}
