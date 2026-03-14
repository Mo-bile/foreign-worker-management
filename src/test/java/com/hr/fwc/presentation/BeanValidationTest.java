package com.hr.fwc.presentation;

import com.hr.fwc.application.service.WorkerRegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkerController.class)
class BeanValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WorkerRegistrationService registrationService;

    private Map<String, Object> validRequestMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Nguyen Van A");
        map.put("passportNumber", "V123456");
        map.put("nationalityCode", "VIETNAM");
        map.put("visaType", "E9");
        map.put("visaExpiryDate", "2026-12-31");
        map.put("entryDate", "2024-01-15");
        map.put("registrationNumber", "12345678901");
        map.put("contractStartDate", "2024-02-01");
        map.put("workplaceId", 1);
        map.put("contactPhone", "010-1234-5678");
        return map;
    }

    @Test
    @DisplayName("이름이_null이면_400_반환")
    void 이름이_null이면_400_반환() throws Exception {
        Map<String, Object> request = validRequestMap();
        request.remove("name");

        mockMvc.perform(post("/api/workers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("국적코드가_빈문자열이면_400_반환")
    void 국적코드가_빈문자열이면_400_반환() throws Exception {
        Map<String, Object> request = validRequestMap();
        request.put("nationalityCode", "");

        mockMvc.perform(post("/api/workers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("비자유형이_null이면_400_반환")
    void 비자유형이_null이면_400_반환() throws Exception {
        Map<String, Object> request = validRequestMap();
        request.remove("visaType");

        mockMvc.perform(post("/api/workers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("입국일이_null이면_400_반환")
    void 입국일이_null이면_400_반환() throws Exception {
        Map<String, Object> request = validRequestMap();
        request.remove("entryDate");

        mockMvc.perform(post("/api/workers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("사업장ID가_null이면_400_반환")
    void 사업장ID가_null이면_400_반환() throws Exception {
        Map<String, Object> request = validRequestMap();
        request.remove("workplaceId");

        mockMvc.perform(post("/api/workers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").exists());
    }
}
