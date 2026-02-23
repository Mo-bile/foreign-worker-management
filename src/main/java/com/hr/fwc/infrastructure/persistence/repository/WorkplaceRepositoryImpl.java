package com.hr.fwc.infrastructure.persistence.repository;

import com.hr.fwc.domain.workplace.Workplace;
import com.hr.fwc.domain.workplace.WorkplaceRepository;
import com.hr.fwc.infrastructure.persistence.mapper.WorkplaceMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class WorkplaceRepositoryImpl implements WorkplaceRepository {

    private final WorkplaceJpaRepository jpaRepository;

    public WorkplaceRepositoryImpl(WorkplaceJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Workplace save(Workplace workplace) {
        return WorkplaceMapper.toDomain(jpaRepository.save(WorkplaceMapper.toEntity(workplace)));
    }

    @Override
    public Optional<Workplace> findById(Long id) {
        return jpaRepository.findById(id).map(WorkplaceMapper::toDomain);
    }

    @Override
    public Optional<Workplace> findByBusinessNumber(String businessNumber) {
        return jpaRepository.findByBusinessNumber(businessNumber).map(WorkplaceMapper::toDomain);
    }
}
