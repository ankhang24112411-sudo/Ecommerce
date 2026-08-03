package com.khang.backendecommerce.newstruc.domain.user.repository;

import com.khang.backendecommerce.newstruc.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    @Query("""
        SELECT DISTINCT u
        FROM UserEntity u
        LEFT JOIN FETCH u.roles userRole
        LEFT JOIN FETCH userRole.role
        WHERE u.username = :username
    """)
    Optional<UserEntity> findByUsernameWithRoles(
            @Param("username") String username
    );
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String username);

    @EntityGraph(
            attributePaths = {"state"},
            type = EntityGraph.EntityGraphType.FETCH
    )
    @Query("""
 SELECT user
 FROM UserEntity user
 where user.id = :userId
""")
    Optional<UserEntity> findByIdWithState(@Param("userId") String userId);
}
