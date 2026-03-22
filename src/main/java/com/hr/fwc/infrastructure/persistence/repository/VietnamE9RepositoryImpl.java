package com.hr.fwc.infrastructure.persistence.repository;

import com.hr.fwc.domain.publicdata.VietnamE9;
import com.hr.fwc.domain.publicdata.VietnamE9Repository;
import com.hr.fwc.infrastructure.persistence.mapper.PublicDataMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class VietnamE9RepositoryImpl implements VietnamE9Repository {
    private final VietnamE9JpaRepository jpaRepository;

    public VietnamE9RepositoryImpl(VietnamE9JpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<VietnamE9> saveAll(List<VietnamE9> items) {
        return jpaRepository.saveAll(items.stream().map(PublicDataMapper::toEntity).toList())
            .stream().map(PublicDataMapper::toDomain).toList();
    }

    @Override
    public List<VietnamE9> findAll() {
        return jpaRepository.findAll().stream().map(PublicDataMapper::toDomain).toList();
    }
}
