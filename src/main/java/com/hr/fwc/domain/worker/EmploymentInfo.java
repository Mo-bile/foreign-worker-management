package com.hr.fwc.domain.worker;

import java.time.LocalDate;
import java.util.Objects;

public class EmploymentInfo {

    private LocalDate contractStartDate;

    private LocalDate contractEndDate;

    private Long workplaceId;

    protected EmploymentInfo() {
    }

    private EmploymentInfo(LocalDate contractStartDate, LocalDate contractEndDate, Long workplaceId) {
        this.contractStartDate = Objects.requireNonNull(contractStartDate, "Contract start date cannot be null");
        this.contractEndDate = contractEndDate;
        this.workplaceId = workplaceId;
    }

    public static EmploymentInfo of(LocalDate contractStartDate, LocalDate contractEndDate, Long workplaceId) {
        return new EmploymentInfo(contractStartDate, contractEndDate, workplaceId);
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

    public Long workplaceId() {
        return workplaceId;
    }

}
