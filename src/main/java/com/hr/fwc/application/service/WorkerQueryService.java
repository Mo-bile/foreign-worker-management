package com.hr.fwc.application.service;

import com.hr.fwc.application.dto.WorkerResponse;
import com.hr.fwc.domain.insurance.InsuranceEligibility;
import com.hr.fwc.domain.insurance.InsuranceEligibilityService;
import com.hr.fwc.domain.worker.ForeignWorker;
import com.hr.fwc.domain.worker.ForeignWorkerRepository;
import com.hr.fwc.domain.worker.WorkerNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class WorkerQueryService {

    private final ForeignWorkerRepository workerRepository;
    private final InsuranceEligibilityService insuranceService;

    public WorkerQueryService(ForeignWorkerRepository workerRepository,
                              InsuranceEligibilityService insuranceService) {
        this.workerRepository = workerRepository;
        this.insuranceService = insuranceService;
    }

    public List<WorkerResponse> getAllWorkers() {
        return workerRepository.findAll().stream()
            .map(this::toWorkerResponse)
            .toList();
    }

    public WorkerResponse getWorkerById(Long id) {
        ForeignWorker worker = workerRepository.findById(id)
            .orElseThrow(() -> new WorkerNotFoundException("근로자를 찾을 수 없습니다. ID: " + id));
        return toWorkerResponse(worker);
    }

    private WorkerResponse toWorkerResponse(ForeignWorker worker) {
        List<InsuranceEligibility> eligibilities = insuranceService.determineAllEligibilities(worker);
        return WorkerResponse.from(worker, eligibilities);
    }
}
