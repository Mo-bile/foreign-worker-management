package com.hr.fwc.infrastructure.persistence.repository;

import com.hr.fwc.domain.publicdata.Manufacturing;
import com.hr.fwc.domain.publicdata.ManufacturingRepository;
import com.hr.fwc.infrastructure.persistence.mapper.PublicDataMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ManufacturingRepositoryImpl implements ManufacturingRepository {
    private final ManufacturingJpaRepository jpaRepository;

    public ManufacturingRepositoryImpl(ManufacturingJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Manufacturing> saveAll(List<Manufacturing> items) {
        return jpaRepository.saveAll(items.stream().map(PublicDataMapper::toEntity).toList())
            .stream().map(PublicDataMapper::toDomain).toList();
    }

    @Override
    public List<Manufacturing> findAll() {
        return jpaRepository.findAll().stream().map(PublicDataMapper::toDomain).toList();
    }
}
