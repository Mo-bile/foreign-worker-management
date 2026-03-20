package com.hr.fwc.presentation;

import com.hr.fwc.application.dto.RegisterWorkerRequest;
import com.hr.fwc.application.dto.WorkerResponse;
import com.hr.fwc.application.service.WorkerQueryService;
import com.hr.fwc.application.service.WorkerRegistrationService;
import com.hr.fwc.domain.worker.WorkerNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkerController.class)
class WorkerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WorkerRegistrationService registrationService;

    @MockBean
    private WorkerQueryService queryService;

    @Test
    @DisplayName("근로자 등록 API 요청")
    void registerWorker() throws Exception {
        RegisterWorkerRequest request = new RegisterWorkerRequest(
            "Nguyen Van A",
            "V123456",
            "VIETNAM",
            "E9",
            LocalDate.of(2026, 12, 31),
            LocalDate.of(2024, 1, 15),
            "12345678901",
            LocalDate.of(2024, 2, 1),
            null,
            1L,
            "010-1234-5678",
            null
        );

        WorkerResponse response = new WorkerResponse(
            1L,
            "Nguyen Van A",
            "베트남",
            "고용허가제 일반외국인",
            "2026-12-31",
            "재직중",
            List.of(
                new WorkerResponse.InsuranceEligibilityDto("국민연금", "의무가입", "테스트"),
                new WorkerResponse.InsuranceEligibilityDto("건강보험", "의무가입", "테스트")
            )
        );

        when(registrationService.registerWorker(any())).thenReturn(response);

        mockMvc.perform(post("/api/workers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Nguyen Van A"))
            .andExpect(jsonPath("$.nationality").value("베트남"))
            .andExpect(jsonPath("$.insuranceEligibilities[0].insuranceType").value("국민연금"));
    }

    @Test
    @DisplayName("근로자 목록 조회 API - 200 OK")
    void 근로자_목록_조회_성공() throws Exception {
        WorkerResponse response = new WorkerResponse(
            1L, "Nguyen Van A", "베트남", "고용허가제 일반외국인",
            "2026-12-31", "재직중",
            List.of(new WorkerResponse.InsuranceEligibilityDto("국민연금", "의무가입", "테스트"))
        );

        when(queryService.getAllWorkers()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/workers"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Nguyen Van A"))
            .andExpect(jsonPath("$[0].insuranceEligibilities[0].insuranceType").value("국민연금"));
    }

    @Test
    @DisplayName("근로자 목록 조회 API - 빈 목록")
    void 근로자_목록이_비어있으면_빈_배열_반환() throws Exception {
        when(queryService.getAllWorkers()).thenReturn(List.of());

        mockMvc.perform(get("/api/workers"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("근로자 상세 조회 API - 200 OK")
    void 근로자_상세_조회_성공() throws Exception {
        WorkerResponse response = new WorkerResponse(
            1L, "Nguyen Van A", "베트남", "고용허가제 일반외국인",
            "2026-12-31", "재직중",
            List.of(
                new WorkerResponse.InsuranceEligibilityDto("국민연금", "의무가입", "테스트"),
                new WorkerResponse.InsuranceEligibilityDto("건강보험", "의무가입", "테스트")
            )
        );

        when(queryService.getWorkerById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/workers/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Nguyen Van A"))
            .andExpect(jsonPath("$.insuranceEligibilities", hasSize(2)));
    }

    @Test
    @DisplayName("근로자 상세 조회 API - 존재하지 않는 ID는 404")
    void 존재하지_않는_근로자_조회시_404_반환() throws Exception {
        when(queryService.getWorkerById(999L))
            .thenThrow(new WorkerNotFoundException("근로자를 찾을 수 없습니다. ID: 999"));

        mockMvc.perform(get("/api/workers/999"))
            .andExpect(status().isNotFound());
    }

}
