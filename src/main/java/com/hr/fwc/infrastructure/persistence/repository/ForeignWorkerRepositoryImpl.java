package com.hr.fwc.infrastructure.persistence.repository;

import com.hr.fwc.domain.worker.ForeignWorker;
import com.hr.fwc.domain.worker.ForeignWorkerRepository;
import com.hr.fwc.domain.worker.Nationality;
import com.hr.fwc.infrastructure.persistence.mapper.ForeignWorkerMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ForeignWorkerRepositoryImpl implements ForeignWorkerRepository {

    private final ForeignWorkerJpaRepository jpaRepository;

    public ForeignWorkerRepositoryImpl(ForeignWorkerJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ForeignWorker save(ForeignWorker worker) {
        return ForeignWorkerMapper.toDomain(jpaRepository.save(ForeignWorkerMapper.toEntity(worker)));
    }

    @Override
    public List<ForeignWorker> findAll() {
        return jpaRepository.findAll()
            .stream()
            .map(ForeignWorkerMapper::toDomain)
            .toList();
    }

    @Override
    public Optional<ForeignWorker> findById(Long id) {
        return jpaRepository.findById(id).map(ForeignWorkerMapper::toDomain);
    }

    @Override
    public List<ForeignWorker> findByCompanyId(Long companyId) {
        return jpaRepository.findByEmploymentInfoCompanyId(companyId)
            .stream()
            .map(ForeignWorkerMapper::toDomain)
            .toList();
    }

    @Override
    public List<ForeignWorker> findByNationality(Nationality nationality) {
        return jpaRepository.findByNationality(nationality)
            .stream()
            .map(ForeignWorkerMapper::toDomain)
            .toList();
    }
}
