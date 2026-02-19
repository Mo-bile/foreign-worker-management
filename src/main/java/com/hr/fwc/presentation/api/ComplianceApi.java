package com.hr.fwc.presentation.api;

import com.hr.fwc.domain.compliance.ComplianceDeadline;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "컴플라이언스 대시보드", description = "데드라인 추적 및 알림 API")
public interface ComplianceApi {

    @Operation(summary = "기한 초과 데드라인 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    ResponseEntity<List<ComplianceDeadline>> getOverdueDeadlines();

    @Operation(summary = "임박한 데드라인 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    ResponseEntity<List<ComplianceDeadline>> getUpcomingDeadlines(
        @Parameter(description = "조회할 일수", example = "30") int days
    );

    @Operation(summary = "특정 근로자 데드라인 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "근로자를 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    ResponseEntity<List<ComplianceDeadline>> getWorkerDeadlines(
        @Parameter(description = "근로자 ID", example = "1") Long workerId
    );

}
