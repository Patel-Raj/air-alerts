package com.cloud.backend.repositories;

import com.cloud.backend.entities.VerifyEmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerifyEmailRepository extends JpaRepository<VerifyEmailEntity, Long> {

    Optional<VerifyEmailEntity> findByUniqueId(String uniqueId);
}
