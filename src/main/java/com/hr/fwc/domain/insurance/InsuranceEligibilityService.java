package com.hr.fwc.domain.insurance;

import com.hr.fwc.domain.worker.ForeignWorker;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InsuranceEligibilityService {

    private final List<InsuranceEligibilityPolicy> policies;

    public InsuranceEligibilityService() {
        this.policies = List.of(
            new NationalPensionPolicy(),
            new HealthInsurancePolicy(),
            new EmploymentInsurancePolicy(),
            new IndustrialAccidentPolicy()
        );
    }

    public List<InsuranceEligibility> determineAllEligibilities(ForeignWorker worker) {
        List<InsuranceEligibility> results = new ArrayList<>();
        for (InsuranceEligibilityPolicy policy : policies) {
            results.add(policy.determineEligibility(worker));
        }
        return results;
    }

    public List<InsuranceEligibility> getMandatoryInsurances(ForeignWorker worker) {
        return determineAllEligibilities(worker).stream()
            .filter(InsuranceEligibility::isMandatory)
            .toList();
    }

}
