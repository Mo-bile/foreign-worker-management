package com.hr.fwc.domain.insurance;

import java.util.Objects;

public class InsuranceEligibility {

    private final InsuranceType insuranceType;
    private final EligibilityStatus status;
    private final String reason;

    private InsuranceEligibility(InsuranceType insuranceType, EligibilityStatus status, String reason) {
        this.insuranceType = Objects.requireNonNull(insuranceType, "Insurance type cannot be null");
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        this.reason = Objects.requireNonNull(reason, "Reason cannot be null");
    }

    public static InsuranceEligibility of(InsuranceType insuranceType, EligibilityStatus status, String reason) {
        return new InsuranceEligibility(insuranceType, status, reason);
    }

    public boolean isMandatory() {
        return status == EligibilityStatus.MANDATORY;
    }

    public InsuranceType insuranceType() {
        return insuranceType;
    }

    public EligibilityStatus status() {
        return status;
    }

    public String reason() {
        return reason;
    }

}
