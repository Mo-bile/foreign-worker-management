package com.hr.fwc.application.service;

import com.hr.fwc.application.dto.CompanyResponse;
import com.hr.fwc.application.dto.CreateCompanyRequest;
import com.hr.fwc.application.dto.UpdateCompanyRequest;
import com.hr.fwc.domain.company.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional
    public CompanyResponse createCompany(CreateCompanyRequest request) {
        companyRepository.findByBusinessNumber(request.businessNumber())
            .ifPresent(existing -> {
                throw new DuplicateBusinessNumberException(request.businessNumber());
            });

        Company company = Company.create(
            request.name(),
            request.businessNumber(),
            Region.valueOf(request.region()),
            request.subRegion(),
            IndustryCategory.valueOf(request.industryCategory()),
            request.industrySubCategory(),
            request.employeeCount(),
            request.foreignWorkerCount(),
            request.address(),
            request.contactPhone()
        );

        return CompanyResponse.from(companyRepository.save(company));
    }

    public CompanyResponse getCompany(Long id) {
        Company company = companyRepository.findById(id)
            .orElseThrow(() -> new CompanyNotFoundException(id));
        return CompanyResponse.from(company);
    }

    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll().stream()
            .map(CompanyResponse::from)
            .toList();
    }

    @Transactional
    public CompanyResponse updateCompany(Long id, UpdateCompanyRequest request) {
        Company existing = companyRepository.findById(id)
            .orElseThrow(() -> new CompanyNotFoundException(id));

        Company updated = existing.updateInfo(
            request.name(),
            Region.valueOf(request.region()),
            request.subRegion(),
            IndustryCategory.valueOf(request.industryCategory()),
            request.industrySubCategory(),
            request.employeeCount(),
            request.foreignWorkerCount(),
            request.address(),
            request.contactPhone()
        );

        return CompanyResponse.from(companyRepository.save(updated));
    }
}
