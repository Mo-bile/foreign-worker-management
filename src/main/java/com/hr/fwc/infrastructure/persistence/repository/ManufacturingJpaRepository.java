package com.hr.fwc.infrastructure.persistence.repository;

import com.hr.fwc.infrastructure.persistence.entity.ManufacturingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturingJpaRepository extends JpaRepository<ManufacturingEntity, Long> {}
