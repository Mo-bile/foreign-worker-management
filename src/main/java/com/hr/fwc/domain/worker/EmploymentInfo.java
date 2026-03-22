package com.hr.fwc.domain.worker;

import java.time.LocalDate;
import java.util.Objects;

public class EmploymentInfo {

    private LocalDate contractStartDate;

    private LocalDate contractEndDate;

    private Long companyId;

    protected EmploymentInfo() {
    }

    private EmploymentInfo(LocalDate contractStartDate, LocalDate contractEndDate, Long companyId) {
        this.contractStartDate = Objects.requireNonNull(contractStartDate, "Contract start date cannot be null");
        this.contractEndDate = contractEndDate;
        this.companyId = companyId;
    }

    public static EmploymentInfo of(LocalDate contractStartDate, LocalDate contractEndDate, Long companyId) {
        return new EmploymentInfo(contractStartDate, contractEndDate, companyId);
    }

    public boolean isActive() {
        LocalDate now = LocalDate.now();
        if (now.isBefore(contractStartDate)) {
            return false;
        }
        if (contractEndDate != null && now.isAfter(contractEndDate)) {
            return false;
        }
        return true;
    }

    public LocalDate contractStartDate() {
        return contractStartDate;
    }

    public LocalDate contractEndDate() {
        return contractEndDate;
    }

    public Long companyId() {
        return companyId;
    }

}
