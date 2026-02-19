package com.hr.fwc.presentation.api;

import com.hr.fwc.application.dto.RegisterWorkerRequest;
import com.hr.fwc.application.dto.WorkerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "근로자 관리", description = "외국인 근로자 등록 및 관리 API")
public interface WorkerApi {

    @Operation(
        summary = "근로자 등록",
        description = "외국인 근로자를 등록하고 4대보험 가입 의무를 자동 판단합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "등록 성공",
            content = @Content(schema = @Schema(implementation = WorkerResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    ResponseEntity<WorkerResponse> registerWorker(RegisterWorkerRequest request);

}
