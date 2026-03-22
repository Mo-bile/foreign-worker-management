package com.hr.fwc.infrastructure.persistence.repository;

import com.hr.fwc.domain.company.Company;
import com.hr.fwc.domain.company.CompanyRepository;
import com.hr.fwc.infrastructure.persistence.mapper.CompanyMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class CompanyRepositoryImpl implements CompanyRepository {

    private final CompanyJpaRepository jpaRepository;

    public CompanyRepositoryImpl(CompanyJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Company save(Company company) {
        return CompanyMapper.toDomain(jpaRepository.save(CompanyMapper.toEntity(company)));
    }

    @Override
    public Optional<Company> findById(Long id) {
        return jpaRepository.findById(id).map(CompanyMapper::toDomain);
    }

    @Override
    public Optional<Company> findByBusinessNumber(String businessNumber) {
        return jpaRepository.findByBusinessNumber(businessNumber).map(CompanyMapper::toDomain);
    }

    @Override
    public List<Company> findAll() {
        return jpaRepository.findAll().stream().map(CompanyMapper::toDomain).toList();
    }
}
