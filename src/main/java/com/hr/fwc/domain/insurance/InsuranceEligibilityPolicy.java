package com.hr.fwc.domain.insurance;

import com.hr.fwc.domain.worker.ForeignWorker;

public interface InsuranceEligibilityPolicy {

    InsuranceEligibility determineEligibility(ForeignWorker worker);

}
