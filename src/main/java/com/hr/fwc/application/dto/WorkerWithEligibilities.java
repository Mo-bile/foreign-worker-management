package com.hr.fwc.application.dto;

import com.hr.fwc.domain.insurance.InsuranceEligibility;
import com.hr.fwc.domain.worker.ForeignWorker;

import java.util.List;

public record WorkerWithEligibilities(
    ForeignWorker worker,
    List<InsuranceEligibility> eligibilities
) {
}
