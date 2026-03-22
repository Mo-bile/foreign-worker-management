package com.hr.fwc.infrastructure.persistence.repository;

import com.hr.fwc.infrastructure.persistence.entity.QuotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuotaJpaRepository extends JpaRepository<QuotaEntity, Long> {
    List<QuotaEntity> findByYear(int year);
}
