package com.hr.fwc.domain.worker;

import java.util.List;
import java.util.Optional;

public interface ForeignWorkerRepository {
    ForeignWorker save(ForeignWorker worker);

    Optional<ForeignWorker> findById(Long id);

    List<ForeignWorker> findByCompanyId(Long companyId);

    List<ForeignWorker> findAll();

    List<ForeignWorker> findByNationality(Nationality nationality);
}
