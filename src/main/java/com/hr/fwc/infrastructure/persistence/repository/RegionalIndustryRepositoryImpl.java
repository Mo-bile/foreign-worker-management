package com.hr.fwc.infrastructure.persistence.repository;

import com.hr.fwc.domain.publicdata.RegionalIndustry;
import com.hr.fwc.domain.publicdata.RegionalIndustryRepository;
import com.hr.fwc.infrastructure.persistence.mapper.PublicDataMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class RegionalIndustryRepositoryImpl implements RegionalIndustryRepository {
    private final RegionalIndustryJpaRepository jpaRepository;

    public RegionalIndustryRepositoryImpl(RegionalIndustryJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<RegionalIndustry> saveAll(List<RegionalIndustry> items) {
        return jpaRepository.saveAll(items.stream().map(PublicDataMapper::toEntity).toList())
            .stream().map(PublicDataMapper::toDomain).toList();
    }

    @Override
    public List<RegionalIndustry> findAll() {
        return jpaRepository.findAll().stream().map(PublicDataMapper::toDomain).toList();
    }
}
