package com.hr.fwc.infrastructure.persistence.repository;

import com.hr.fwc.infrastructure.persistence.entity.WorkplaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkplaceJpaRepository extends JpaRepository<WorkplaceEntity, Long> {
    Optional<WorkplaceEntity> findByBusinessNumber(String businessNumber);
}
