package com.hr.fwc.presentation;

import com.hr.fwc.application.dto.RegisterWorkerRequest;
import com.hr.fwc.application.dto.WorkerResponse;
import com.hr.fwc.application.service.WorkerQueryService;
import com.hr.fwc.application.service.WorkerRegistrationService;
import com.hr.fwc.presentation.api.WorkerApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workers")
public class WorkerController implements WorkerApi {

    private final WorkerRegistrationService registrationService;
    private final WorkerQueryService queryService;

    public WorkerController(WorkerRegistrationService registrationService,
                            WorkerQueryService queryService) {
        this.registrationService = registrationService;
        this.queryService = queryService;
    }

    @PostMapping
    public ResponseEntity<WorkerResponse> registerWorker(@RequestBody RegisterWorkerRequest request) {
        WorkerResponse response = registrationService.registerWorker(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<WorkerResponse>> getAllWorkers() {
        List<WorkerResponse> workers = queryService.getAllWorkers();
        return ResponseEntity.ok(workers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkerResponse> getWorkerById(@PathVariable Long id) {
        WorkerResponse worker = queryService.getWorkerById(id);
        return ResponseEntity.ok(worker);
    }

}
