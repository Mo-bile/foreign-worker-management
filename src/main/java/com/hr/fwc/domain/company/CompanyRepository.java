package com.hr.fwc.domain.company;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository {
    Company save(Company company);
    Optional<Company> findById(Long id);
    Optional<Company> findByBusinessNumber(String businessNumber);
    List<Company> findAll();
}
