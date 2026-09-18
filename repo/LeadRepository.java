package com.leadback.repo;

import com.leadback.domain.Business;
import com.leadback.domain.Lead;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeadRepository extends JpaRepository<Lead, Long> {
    Optional<Lead> findTopByBusinessAndPhoneOrderByCreatedAtDesc(Business b, String phone);
    List<Lead> findAllByBusinessOrderByCreatedAtDesc(Business b);
    long countByBusiness(Business b);
}
