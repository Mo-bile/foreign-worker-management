package com.hr.fwc.domain.insurance;

import com.hr.fwc.domain.worker.ForeignWorker;

public class NationalPensionPolicy implements InsuranceEligibilityPolicy {

    @Override
    public InsuranceEligibility determineEligibility(ForeignWorker worker) {
        if (worker.hasSocialSecurityAgreement()) {
            return InsuranceEligibility.of(
                InsuranceType.NATIONAL_PENSION,
                EligibilityStatus.EXEMPT,
                "사회보장협정 체결국 국적자로 본국 연금제도 적용받음"
            );
        }

        return InsuranceEligibility.of(
            InsuranceType.NATIONAL_PENSION,
            EligibilityStatus.MANDATORY,
            "18세 이상 60세 미만 외국인 근로자는 국민연금 의무가입대상"
        );
    }

}
