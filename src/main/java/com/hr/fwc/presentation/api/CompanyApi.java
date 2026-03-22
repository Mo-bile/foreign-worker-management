package com.hr.fwc.presentation.api;

import com.hr.fwc.application.dto.CompanyResponse;
import com.hr.fwc.application.dto.CreateCompanyRequest;
import com.hr.fwc.application.dto.UpdateCompanyRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "사업장 관리", description = "사업장 등록 및 관리 API")
public interface CompanyApi {

    @Operation(summary = "사업장 등록")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "등록 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "409", description = "중복 사업자등록번호")
    })
    ResponseEntity<CompanyResponse> createCompany(CreateCompanyRequest request);

    @Operation(summary = "사업장 목록 조회")
    ResponseEntity<List<CompanyResponse>> getAllCompanies();

    @Operation(summary = "사업장 상세 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "사업장을 찾을 수 없음")
    })
    ResponseEntity<CompanyResponse> getCompanyById(
        @Parameter(description = "사업장 ID", required = true) Long id
    );

    @Operation(summary = "사업장 정보 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공"),
        @ApiResponse(responseCode = "404", description = "사업장을 찾을 수 없음")
    })
    ResponseEntity<CompanyResponse> updateCompany(
        @Parameter(description = "사업장 ID", required = true) Long id,
        UpdateCompanyRequest request
    );
}
