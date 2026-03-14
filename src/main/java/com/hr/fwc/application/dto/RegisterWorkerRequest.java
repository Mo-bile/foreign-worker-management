package com.hr.fwc.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "외국인 근로자 등록 요청")
public record RegisterWorkerRequest(
    @NotBlank(message = "이름은 필수입니다")
    @Schema(description = "이름", example = "Nguyen Van A", requiredMode = Schema.RequiredMode.REQUIRED)
    String name,

    @Schema(description = "여권번호", example = "V12345678")
    String passportNumber,

    @NotBlank(message = "국적 코드는 필수입니다")
    @Schema(description = "국적 코드", example = "VIETNAM", requiredMode = Schema.RequiredMode.REQUIRED)
    String nationalityCode,

    @NotBlank(message = "비자 유형은 필수입니다")
    @Schema(description = "비자 유형", example = "E9", requiredMode = Schema.RequiredMode.REQUIRED)
    String visaType,

    @NotNull(message = "비자 만료일은 필수입니다")
    @Schema(description = "비자 만료일", example = "2026-12-31", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate visaExpiryDate,

    @NotNull(message = "입국일은 필수입니다")
    @Schema(description = "입국일", example = "2024-01-15", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate entryDate,

    @Schema(description = "외국인등록번호", example = "12345678901")
    String registrationNumber,

    @NotNull(message = "근로계약 시작일은 필수입니다")
    @Schema(description = "근로계약 시작일", example = "2024-02-01", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate contractStartDate,

    @Schema(description = "근로계약 종료일", example = "2026-12-31")
    LocalDate contractEndDate,

    @NotNull(message = "사업장 ID는 필수입니다")
    @Schema(description = "사업장 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long workplaceId,

    @Schema(description = "연락처", example = "010-1234-5678")
    String contactPhone,

    @Schema(description = "이메일", example = "worker@example.com")
    String contactEmail
) {}
