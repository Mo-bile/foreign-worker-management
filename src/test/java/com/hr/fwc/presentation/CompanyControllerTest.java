package com.hr.fwc.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hr.fwc.application.dto.CreateCompanyRequest;
import com.hr.fwc.application.dto.UpdateCompanyRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/companies — 사업장 등록 성공")
    void 사업장_등록_성공() throws Exception {
        CreateCompanyRequest request = new CreateCompanyRequest(
            "테스트회사", "111-11-11111", "SEOUL", null,
            "MANUFACTURING", null, 50, 10, "서울시", "02-0000-0000"
        );

        mockMvc.perform(post("/api/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.name").value("테스트회사"))
            .andExpect(jsonPath("$.regionName").value("서울특별시"));
    }

    @Test
    @DisplayName("GET /api/companies — 사업장 목록 조회")
    void 사업장_목록_조회() throws Exception {
        mockMvc.perform(get("/api/companies"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(8))));
    }

    @Test
    @DisplayName("GET /api/companies/{id} — 사업장 상세 조회")
    void 사업장_상세_조회() throws Exception {
        mockMvc.perform(get("/api/companies/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.businessNumber").value("123-45-67890"));
    }

    @Test
    @DisplayName("PUT /api/companies/{id} — 사업장 수정 성공")
    void 사업장_수정_성공() throws Exception {
        UpdateCompanyRequest request = new UpdateCompanyRequest(
            "변경이름", "BUSAN", null,
            "CONSTRUCTION", null, 200, 50, "부산", "051-000"
        );

        mockMvc.perform(put("/api/companies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("변경이름"))
            .andExpect(jsonPath("$.businessNumber").value("123-45-67890"));
    }

    @Test
    @DisplayName("POST /api/companies — 유효성 검증 실패 시 400")
    void 유효성_검증_실패() throws Exception {
        CreateCompanyRequest invalid = new CreateCompanyRequest(
            "", "invalid", "SEOUL", null,
            "MANUFACTURING", null, 0, -1, "", ""
        );

        mockMvc.perform(post("/api/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/companies/99999 — 존재하지 않는 사업장 404")
    void 존재하지_않는_사업장_404() throws Exception {
        mockMvc.perform(get("/api/companies/99999"))
            .andExpect(status().isNotFound());
    }
}
