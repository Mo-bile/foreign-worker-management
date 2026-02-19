package com.hr.fwc.domain.insurance;

import com.hr.fwc.domain.worker.ForeignWorker;

public class IndustrialAccidentPolicy implements InsuranceEligibilityPolicy {

    @Override
    public InsuranceEligibility determineEligibility(ForeignWorker worker) {
        return InsuranceEligibility.of(
            InsuranceType.INDUSTRIAL_ACCIDENT,
            EligibilityStatus.MANDATORY,
            "외국인 근로자도 산재보험 의무가입대상 (내국인과 동일)"
        );
    }

}
