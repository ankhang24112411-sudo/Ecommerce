package com.khang.backendecommerce.newstruc.domain.order.dto.realtime;

import com.khang.backendecommerce.newstruc.domain.user.entity.UserEntity;

public record RecipientSnapshot(String receiverName,
                                String phone,
                                String address,
                                String stateName) {
public static RecipientSnapshot from (UserEntity user){
    return new RecipientSnapshot(user.getFirstName().concat(" ").concat(
            user.getLastName()) ,
            user.getPhone() ,
            user.getAddress() ,
            user.getState().getName());
}
}