package com.hr.fwc.application.dto;

import com.hr.fwc.domain.company.IndustryCategory;
import com.hr.fwc.domain.company.Region;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "사업장 등록 요청")
public record CreateCompanyRequest(
    @NotBlank @Size(max = 100)
    @Schema(description = "사업장명", example = "한국제조(주)", requiredMode = Schema.RequiredMode.REQUIRED)
    String name,

    @NotBlank @Pattern(regexp = "\\d{3}-\\d{2}-\\d{5}", message = "사업자등록번호 형식이 올바르지 않습니다 (XXX-XX-XXXXX)")
    @Schema(description = "사업자등록번호", example = "123-45-67890", requiredMode = Schema.RequiredMode.REQUIRED)
    String businessNumber,

    @NotNull
    @Schema(description = "시도", example = "GYEONGGI", requiredMode = Schema.RequiredMode.REQUIRED)
    Region region,

    @Schema(description = "시군구", example = "안산시")
    String subRegion,

    @NotNull
    @Schema(description = "업종 대분류", example = "MANUFACTURING", requiredMode = Schema.RequiredMode.REQUIRED)
    IndustryCategory industryCategory,

    @Schema(description = "업종 중분류", example = "금속가공제품제조업")
    String industrySubCategory,

    @Min(1)
    @Schema(description = "상시 근로자 수", example = "100", requiredMode = Schema.RequiredMode.REQUIRED)
    int employeeCount,

    @Min(0)
    @Schema(description = "외국인 근로자 수 (상시 근로자 수 이하)", example = "20", requiredMode = Schema.RequiredMode.REQUIRED)
    int foreignWorkerCount,

    @NotBlank
    @Schema(description = "주소", example = "경기도 안산시 단원구 공단1로 10", requiredMode = Schema.RequiredMode.REQUIRED)
    String address,

    @NotBlank
    @Schema(description = "연락처", example = "031-123-4567", requiredMode = Schema.RequiredMode.REQUIRED)
    String contactPhone
) {
    @AssertTrue(message = "외국인 근로자 수는 상시 근로자 수를 초과할 수 없습니다")
    @Schema(hidden = true)
    boolean isValidWorkerCount() {
        return foreignWorkerCount <= employeeCount;
    }
}
