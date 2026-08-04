package com.khang.backendecommerce.newstruc.repo;

import com.khang.backendecommerce.newstruc.entity.PaymentEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity,String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(
            attributePaths = {
                    "order",
            }
    )
    Optional<PaymentEntity> findByPaymentReferenceAndUser_Id(String paymentReference , String userId);
}
