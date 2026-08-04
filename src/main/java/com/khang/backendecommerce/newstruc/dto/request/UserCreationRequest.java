package com.khang.backendecommerce.newstruc.dto.request;

import com.khang.backendecommerce.newstruc.entity.StateEntity;
import com.khang.backendecommerce.infrastructure.common.enums.Gender;
import com.khang.backendecommerce.infrastructure.util.GenderSubset;
import com.khang.backendecommerce.infrastructure.util.PhoneNumber;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import static com.khang.backendecommerce.infrastructure.common.enums.Gender.*;

@Getter
@Setter
@NoArgsConstructor
public class UserCreationRequest {

    @NotBlank(message = "firstName must be not blank")
    private String firstName;

    @NotBlank(message = "firstName must be not blank")
    private String lastName;

    @GenderSubset(anyOf = {MALE, FEMALE, OTHER})
    private Gender gender;

    @NotNull(message = "dateOfBirth must be not null")
    @Past(message = "dateOfBirth must be in the past")
    private LocalDate birthDay;

    @PhoneNumber(message = "phone invalid format")
    private String phone;

    @NotNull(message = "Address must be not null")
    private String address;

    private String profilePicture;

    @Email(message = "email invalid format")
    private String email;

    @NotNull(message = "username must be not null")
    private String username;

    @NotNull(message = "password must be not null")

    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    private StateEntity state;
}
