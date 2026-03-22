package com.hr.fwc.infrastructure.persistence.repository;

import com.hr.fwc.infrastructure.persistence.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, Long> {
    Optional<CompanyEntity> findByBusinessNumber(String businessNumber);
}
