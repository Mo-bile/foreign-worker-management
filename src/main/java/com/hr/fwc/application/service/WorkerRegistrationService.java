package com.hr.fwc.application.service;

import com.hr.fwc.application.dto.RegisterWorkerRequest;
import com.hr.fwc.application.dto.WorkerResponse;
import com.hr.fwc.domain.compliance.ComplianceDeadline;
import com.hr.fwc.domain.compliance.ComplianceDeadlineRepository;
import com.hr.fwc.domain.compliance.DeadlineType;
import com.hr.fwc.domain.insurance.InsuranceEligibility;
import com.hr.fwc.domain.insurance.InsuranceEligibilityService;
import com.hr.fwc.domain.worker.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class WorkerRegistrationService {

    private final ForeignWorkerRepository workerRepository;
    private final InsuranceEligibilityService insuranceService;
    private final ComplianceDeadlineRepository deadlineRepository;

    public WorkerRegistrationService(ForeignWorkerRepository workerRepository,
                                     InsuranceEligibilityService insuranceService,
                                     ComplianceDeadlineRepository deadlineRepository) {
        this.workerRepository = workerRepository;
        this.insuranceService = insuranceService;
        this.deadlineRepository = deadlineRepository;
    }

    public WorkerResponse registerWorker(RegisterWorkerRequest request) {
        PersonalInfo personalInfo = PersonalInfo.of(
            request.name(),
            request.passportNumber(),
            request.contactPhone(),
            request.contactEmail()
        );

        VisaInfo visaInfo = VisaInfo.of(
            VisaType.valueOf(request.visaType()),
            request.visaExpiryDate(),
            request.entryDate(),
            request.registrationNumber()
        );

        EmploymentInfo employmentInfo = EmploymentInfo.of(
            request.contractStartDate(),
            request.contractEndDate(),
            request.workplaceId()
        );

        Nationality nationality = Nationality.valueOf(request.nationalityCode());

        ForeignWorker worker = ForeignWorker.create(personalInfo, visaInfo, employmentInfo, nationality);
        ForeignWorker savedWorker = workerRepository.save(worker);

        List<InsuranceEligibility> eligibilities = insuranceService.determineAllEligibilities(savedWorker);

        createComplianceDeadlines(savedWorker);

        return WorkerResponse.from(savedWorker, eligibilities);
    }

    private void createComplianceDeadlines(ForeignWorker worker) {
        ComplianceDeadline visaDeadline = ComplianceDeadline.create(
            worker.id(),
            DeadlineType.VISA_EXPIRY,
            worker.visaInfo().visaExpiryDate(),
            "비자 갱신 필요"
        );
        deadlineRepository.save(visaDeadline);

        ComplianceDeadline insuranceDeadline = ComplianceDeadline.create(
            worker.id(),
            DeadlineType.INSURANCE_ENROLLMENT,
            worker.visaInfo().entryDate().plusDays(30),
            "4대보험 가입 기한"
        );
        deadlineRepository.save(insuranceDeadline);
    }

}
