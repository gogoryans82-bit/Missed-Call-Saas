package com.leadback.repo;

import com.leadback.domain.Business;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Long> {
    Optional<Business> findByTwilioNumber(String twilioNumber);
    Optional<Business> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByTwilioNumber(String twilioNumber);
}
