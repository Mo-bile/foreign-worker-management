package com.hr.fwc.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "사업장 수정 요청")
public record UpdateCompanyRequest(
    @NotBlank @Size(max = 100)
    @Schema(description = "사업장명", example = "한국제조(주)")
    String name,

    @NotNull
    @Schema(description = "시도", example = "GYEONGGI")
    String region,

    @Schema(description = "시군구", example = "안산시")
    String subRegion,

    @NotNull
    @Schema(description = "업종 대분류", example = "MANUFACTURING")
    String industryCategory,

    @Schema(description = "업종 중분류", example = "금속가공제품제조업")
    String industrySubCategory,

    @Min(1)
    @Schema(description = "상시 근로자 수", example = "100")
    int employeeCount,

    @Min(0)
    @Schema(description = "외국인 근로자 수", example = "20")
    int foreignWorkerCount,

    @NotBlank
    @Schema(description = "주소", example = "경기도 안산시 단원구 공단1로 10")
    String address,

    @NotBlank
    @Schema(description = "연락처", example = "031-123-4567")
    String contactPhone
) {}
