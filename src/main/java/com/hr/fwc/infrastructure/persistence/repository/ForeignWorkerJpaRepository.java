package com.hr.fwc.infrastructure.persistence.repository;

import com.hr.fwc.domain.worker.Nationality;
import com.hr.fwc.infrastructure.persistence.entity.ForeignWorkerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForeignWorkerJpaRepository extends JpaRepository<ForeignWorkerEntity, Long> {
    List<ForeignWorkerEntity> findByEmploymentInfoWorkplaceId(Long workplaceId);

    List<ForeignWorkerEntity> findByNationality(Nationality nationality);
}
