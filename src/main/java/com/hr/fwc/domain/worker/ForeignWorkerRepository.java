package com.hr.fwc.domain.worker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForeignWorkerRepository extends JpaRepository<ForeignWorker, Long> {

    List<ForeignWorker> findByEmploymentInfoWorkplaceId(Long workplaceId);

    List<ForeignWorker> findByNationality(Nationality nationality);

}
