package com.hr.fwc.application.dto;

import com.hr.fwc.domain.compliance.ComplianceDeadline;
import com.hr.fwc.domain.compliance.DeadlineStatus;
import com.hr.fwc.domain.compliance.DeadlineType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ComplianceDeadlineResponseTest {

    @Test
    @DisplayName("도메인_객체를_DTO로_변환")
    void 도메인_객체를_DTO로_변환() {
        LocalDate dueDate = LocalDate.of(2026, 6, 15);
        ComplianceDeadline deadline = ComplianceDeadline.reconstitute(
            1L, 100L, DeadlineType.VISA_EXPIRY, dueDate, DeadlineStatus.APPROACHING, "비자 갱신 필요"
        );

        ComplianceDeadlineResponse response = ComplianceDeadlineResponse.from(deadline);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.workerId()).isEqualTo(100L);
        assertThat(response.deadlineType()).isEqualTo("VISA_EXPIRY");
        assertThat(response.dueDate()).isEqualTo("2026-06-15");
        assertThat(response.status()).isEqualTo("APPROACHING");
        assertThat(response.description()).isEqualTo("비자 갱신 필요");
    }

    @Test
    @DisplayName("모든_데드라인_타입과_상태가_문자열로_변환")
    void 모든_데드라인_타입과_상태가_문자열로_변환() {
        ComplianceDeadline deadline = ComplianceDeadline.reconstitute(
            2L, 200L, DeadlineType.INSURANCE_ENROLLMENT,
            LocalDate.of(2026, 3, 1), DeadlineStatus.OVERDUE, "보험 가입 기한 초과"
        );

        ComplianceDeadlineResponse response = ComplianceDeadlineResponse.from(deadline);

        assertThat(response.deadlineType()).isEqualTo("INSURANCE_ENROLLMENT");
        assertThat(response.status()).isEqualTo("OVERDUE");
        assertThat(response.dueDate()).isEqualTo("2026-03-01");
    }
}
