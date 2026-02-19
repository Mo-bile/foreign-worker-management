package com.hr.fwc.domain.insurance;

import com.hr.fwc.domain.worker.ForeignWorker;

public class HealthInsurancePolicy implements InsuranceEligibilityPolicy {

    @Override
    public InsuranceEligibility determineEligibility(ForeignWorker worker) {
        return InsuranceEligibility.of(
            InsuranceType.HEALTH_INSURANCE,
            EligibilityStatus.MANDATORY,
            "외국인 근로자도 내국인과 동일하게 건강보험 의무가입대상"
        );
    }

}
