package com.hr.fwc.application.dto;

import com.hr.fwc.domain.insurance.InsuranceEligibility;
import com.hr.fwc.domain.worker.ForeignWorker;

import java.util.List;

public record WorkerResponse(
    Long id,
    String name,
    String nationality,
    String visaType,
    String visaExpiryDate,
    String status,
    List<InsuranceEligibilityDto> insuranceEligibilities
) {

    public record InsuranceEligibilityDto(
        String insuranceType,
        String status,
        String reason
    ) {
        public static InsuranceEligibilityDto from(InsuranceEligibility eligibility) {
            return new InsuranceEligibilityDto(
                eligibility.insuranceType().koreanName(),
                eligibility.status().koreanName(),
                eligibility.reason()
            );
        }
    }

    public static WorkerResponse from(ForeignWorker worker, List<InsuranceEligibility> eligibilities) {
        return new WorkerResponse(
            worker.id(),
            worker.personalInfo().name(),
            worker.nationality().koreanName(),
            worker.visaInfo().visaType().description(),
            worker.visaInfo().visaExpiryDate().toString(),
            worker.employmentInfo().isActive() ? "재직중" : "퇴사",
            eligibilities.stream()
                .map(InsuranceEligibilityDto::from)
                .toList()
        );
    }

}
