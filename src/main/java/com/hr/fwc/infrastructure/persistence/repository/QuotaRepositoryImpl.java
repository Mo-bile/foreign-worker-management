package com.hr.fwc.infrastructure.persistence.repository;

import com.hr.fwc.domain.publicdata.Quota;
import com.hr.fwc.domain.publicdata.QuotaRepository;
import com.hr.fwc.infrastructure.persistence.mapper.PublicDataMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class QuotaRepositoryImpl implements QuotaRepository {
    private final QuotaJpaRepository jpaRepository;

    public QuotaRepositoryImpl(QuotaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Quota> findAll() {
        return jpaRepository.findAll().stream().map(PublicDataMapper::toDomain).toList();
    }

    @Override
    public List<Quota> findByYear(int year) {
        return jpaRepository.findByYear(year).stream().map(PublicDataMapper::toDomain).toList();
    }
}
