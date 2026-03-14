package com.hr.fwc.application.service;

import com.hr.fwc.application.dto.RegisterWorkerRequest;
import com.hr.fwc.domain.compliance.ComplianceDeadlineRepository;
import com.hr.fwc.domain.insurance.InsuranceEligibilityService;
import com.hr.fwc.domain.worker.ForeignWorkerRepository;
import com.hr.fwc.domain.worker.InvalidNationalityException;
import com.hr.fwc.domain.worker.InvalidVisaTypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class WorkerRegistrationServiceEnumValidationTest {

    @Mock
    private ForeignWorkerRepository workerRepository;

    @Mock
    private InsuranceEligibilityService insuranceService;

    @Mock
    private ComplianceDeadlineRepository deadlineRepository;

    @InjectMocks
    private WorkerRegistrationService registrationService;

    private RegisterWorkerRequest requestWith(String visaType, String nationalityCode) {
        return new RegisterWorkerRequest(
            "Nguyen Van A",
            "V123456",
            nationalityCode,
            visaType,
            LocalDate.of(2026, 12, 31),
            LocalDate.of(2024, 1, 15),
            "12345678901",
            LocalDate.of(2024, 2, 1),
            null,
            1L,
            "010-1234-5678",
            null
        );
    }

    @Test
    @DisplayName("잘못된_비자유형_등록시_InvalidVisaTypeException_발생")
    void 잘못된_비자유형_등록시_InvalidVisaTypeException_발생() {
        RegisterWorkerRequest request = requestWith("INVALID_VISA", "VIETNAM");

        InvalidVisaTypeException exception = assertThrows(
            InvalidVisaTypeException.class,
            () -> registrationService.registerWorker(request)
        );

        assertTrue(exception.getMessage().contains("INVALID_VISA"));
    }

    @Test
    @DisplayName("잘못된_국적코드_등록시_InvalidNationalityException_발생")
    void 잘못된_국적코드_등록시_InvalidNationalityException_발생() {
        RegisterWorkerRequest request = requestWith("E9", "INVALID_COUNTRY");

        InvalidNationalityException exception = assertThrows(
            InvalidNationalityException.class,
            () -> registrationService.registerWorker(request)
        );

        assertTrue(exception.getMessage().contains("INVALID_COUNTRY"));
    }
}
