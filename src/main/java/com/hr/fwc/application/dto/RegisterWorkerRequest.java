package com.hr.fwc.application.dto;

import java.time.LocalDate;

public record RegisterWorkerRequest(
    String name,
    String passportNumber,
    String nationalityCode,
    String visaType,
    LocalDate visaExpiryDate,
    LocalDate entryDate,
    String registrationNumber,
    LocalDate contractStartDate,
    LocalDate contractEndDate,
    Long workplaceId,
    String contactPhone,
    String contactEmail
) {}
