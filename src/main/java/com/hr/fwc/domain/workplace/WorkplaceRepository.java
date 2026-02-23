package com.hr.fwc.domain.workplace;

import java.util.Optional;

public interface WorkplaceRepository {
    Workplace save(Workplace workplace);

    Optional<Workplace> findById(Long id);

    Optional<Workplace> findByBusinessNumber(String businessNumber);
}
