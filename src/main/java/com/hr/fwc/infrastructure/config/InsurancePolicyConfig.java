package com.hr.fwc.infrastructure.config;

import com.hr.fwc.domain.insurance.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class InsurancePolicyConfig {

    @Bean
    public List<InsuranceEligibilityPolicy> insurancePolicies() {
        return List.of(
            new NationalPensionPolicy(),
            new HealthInsurancePolicy(),
            new EmploymentInsurancePolicy(),
            new IndustrialAccidentPolicy()
        );
    }

}
