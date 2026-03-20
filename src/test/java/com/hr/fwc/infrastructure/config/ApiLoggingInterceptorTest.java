package com.hr.fwc.infrastructure.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(OutputCaptureExtension.class)
@DisplayName("ApiLoggingInterceptor 단위 테스트")
class ApiLoggingInterceptorTest {

    private ApiLoggingInterceptor interceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        interceptor = new ApiLoggingInterceptor();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("preHandle이 startTime attribute를 설정하고 true를 반환한다")
    void preHandle이_startTime_attribute를_설정하고_true를_반환한다() throws Exception {
        request.setMethod("GET");
        request.setRequestURI("/api/workers");

        boolean result = interceptor.preHandle(request, response, new Object());

        assertThat(result).isTrue();
        assertThat(request.getAttribute("startTime")).isNotNull();
        assertThat(request.getAttribute("startTime")).isInstanceOf(Long.class);
    }

    @Test
    @DisplayName("afterCompletion이 [API] 형식의 로그를 출력한다")
    void afterCompletion이_로그를_출력한다(CapturedOutput output) throws Exception {
        request.setMethod("POST");
        request.setRequestURI("/api/workers");
        response.setStatus(201);
        request.setAttribute("startTime", System.currentTimeMillis() - 10);

        interceptor.afterCompletion(request, response, new Object(), null);

        assertThat(output.getAll()).contains("[API]");
        assertThat(output.getAll()).contains("POST");
        assertThat(output.getAll()).contains("/api/workers");
        assertThat(output.getAll()).contains("201");
    }

    @Test
    @DisplayName("쿼리스트링이 있으면 URI에 포함된다")
    void 쿼리스트링이_있으면_URI에_포함된다(CapturedOutput output) throws Exception {
        request.setMethod("GET");
        request.setRequestURI("/api/compliance/upcoming");
        request.setQueryString("days=30");
        response.setStatus(200);
        request.setAttribute("startTime", System.currentTimeMillis() - 5);

        interceptor.afterCompletion(request, response, new Object(), null);

        assertThat(output.getAll()).contains("[API]");
        assertThat(output.getAll()).contains("GET");
        assertThat(output.getAll()).contains("/api/compliance/upcoming?days=30");
        assertThat(output.getAll()).contains("200");
    }
}
