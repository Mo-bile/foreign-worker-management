package com.hr.fwc.infrastructure.persistence.repository;

import com.hr.fwc.infrastructure.persistence.entity.RegionalIndustryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionalIndustryJpaRepository extends JpaRepository<RegionalIndustryEntity, Long> {}
