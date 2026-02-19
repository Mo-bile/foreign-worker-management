package com.hr.fwc.domain.workplace;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkplaceRepository extends JpaRepository<Workplace, Long> {

    Optional<Workplace> findByBusinessNumber(String businessNumber);

}
