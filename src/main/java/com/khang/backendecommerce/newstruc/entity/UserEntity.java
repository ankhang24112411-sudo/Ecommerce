package com.khang.backendecommerce.newstruc.entity;

import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import com.khang.backendecommerce.infrastructure.common.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="tbl_user")
public class UserEntity extends AbstractEntity<String> implements UserDetails,Serializable {
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "gender", nullable = false, columnDefinition = "gender_type")
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate birthDay;

    @Column(name = "phone")
    private Long phone;

    @Column(name = "address")
    private String address;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "email")
    private String email;

    @Column (name = "username")
    private String username;

    @Column (name = "password")
    private String password;

    @OneToOne(mappedBy = "user")
    private CartEntity cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    private StateEntity state;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private Set<UserHasRole> roles = new HashSet<>();
    public String getFullName(){
        return firstName.concat(" ").concat(lastName);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(UserHasRole::getRole)
                .map(RoleEntity::getName)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
       return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getDeleted() == null || getDeleted() == 0;
    }
}
