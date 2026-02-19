package com.hr.fwc.domain.insurance;

import com.hr.fwc.domain.worker.ForeignWorker;
import com.hr.fwc.domain.worker.VisaType;

public class EmploymentInsurancePolicy implements InsuranceEligibilityPolicy {

    @Override
    public InsuranceEligibility determineEligibility(ForeignWorker worker) {
        VisaType visaType = worker.visaInfo().visaType();

        if (isMandatoryVisaType(visaType)) {
            return InsuranceEligibility.of(
                InsuranceType.EMPLOYMENT_INSURANCE,
                EligibilityStatus.MANDATORY,
                "F-2(거주), F-5(영주), F-6(결혼이민) 체류자격은 고용보험 강제가입대상"
            );
        }

        return InsuranceEligibility.of(
            InsuranceType.EMPLOYMENT_INSURANCE,
            EligibilityStatus.OPTIONAL,
            "E-9, H-2, E-7 등 일반 외국인근로자는 임의가입 가능"
        );
    }

    private boolean isMandatoryVisaType(VisaType visaType) {
        return visaType == VisaType.F2 
            || visaType == VisaType.F5 
            || visaType == VisaType.F6;
    }

}
