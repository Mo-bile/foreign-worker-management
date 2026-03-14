package com.hr.fwc.presentation;

import com.hr.fwc.application.dto.RegisterWorkerRequest;
import com.hr.fwc.application.dto.WorkerResponse;
import com.hr.fwc.application.service.WorkerRegistrationService;
import com.hr.fwc.presentation.api.WorkerApi;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workers")
public class WorkerController implements WorkerApi {

    private final WorkerRegistrationService registrationService;

    public WorkerController(WorkerRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<WorkerResponse> registerWorker(@Valid @RequestBody RegisterWorkerRequest request) {
        WorkerResponse response = registrationService.registerWorker(request);
        return ResponseEntity.ok(response);
    }

}
