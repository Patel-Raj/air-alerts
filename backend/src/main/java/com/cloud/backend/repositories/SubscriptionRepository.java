package com.cloud.backend.repositories;

import com.cloud.backend.entities.SubscriptionEntity;
import com.cloud.backend.enumerations.PaymentStatus;
import com.cloud.backend.enumerations.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

    Optional<SubscriptionEntity> findByUserIdAndLocationIdAndParameterIdAndSubscriptionStatus(Long userId, Long locationId, Long parameterId, SubscriptionStatus subscriptionStatus);

}
