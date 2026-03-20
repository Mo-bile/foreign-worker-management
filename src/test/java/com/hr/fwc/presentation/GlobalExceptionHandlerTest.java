package com.hr.fwc.presentation;

import com.hr.fwc.application.dto.RegisterWorkerRequest;
import com.hr.fwc.application.service.WorkerQueryService;
import com.hr.fwc.application.service.WorkerRegistrationService;
import com.hr.fwc.domain.worker.InvalidNationalityException;
import com.hr.fwc.domain.worker.InvalidVisaTypeException;
import com.hr.fwc.domain.worker.WorkerNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkerController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WorkerRegistrationService registrationService;

    @MockBean
    private WorkerQueryService queryService;

    private RegisterWorkerRequest sampleRequest() {
        return new RegisterWorkerRequest(
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
    }

    @Test
    @DisplayName("잘못된_비자유형_요청시_400_반환")
    void 잘못된_비자유형_요청시_400_반환() throws Exception {
        when(registrationService.registerWorker(any()))
            .thenThrow(new InvalidVisaTypeException("유효하지 않은 비자 유형입니다: INVALID_VISA"));

        mockMvc.perform(post("/api/workers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRequest())))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").exists())
            .andExpect(jsonPath("$.error").exists())
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("잘못된_국적코드_요청시_400_반환")
    void 잘못된_국적코드_요청시_400_반환() throws Exception {
        when(registrationService.registerWorker(any()))
            .thenThrow(new InvalidNationalityException("유효하지 않은 국적 코드입니다: INVALID_COUNTRY"));

        mockMvc.perform(post("/api/workers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRequest())))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").exists())
            .andExpect(jsonPath("$.error").exists())
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("존재하지_않는_근로자_조회시_404_반환")
    void 존재하지_않는_근로자_조회시_404_반환() throws Exception {
        when(registrationService.registerWorker(any()))
            .thenThrow(new WorkerNotFoundException("근로자를 찾을 수 없습니다: 999"));

        mockMvc.perform(post("/api/workers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRequest())))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").exists())
            .andExpect(jsonPath("$.error").exists())
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("서버_내부_오류시_500_반환")
    void 서버_내부_오류시_500_반환() throws Exception {
        when(registrationService.registerWorker(any()))
            .thenThrow(new RuntimeException("예상치 못한 서버 오류"));

        mockMvc.perform(post("/api/workers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRequest())))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.status").value(500))
            .andExpect(jsonPath("$.error").value("Internal Server Error"))
            .andExpect(jsonPath("$.message").value("서버 내부 오류가 발생했습니다"));
    }
}
