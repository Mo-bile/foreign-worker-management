package com.hr.fwc.domain.compliance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ComplianceDeadlineTest {

    @Test
    @DisplayName("D-90 이상이면 PENDING 상태")
    void farFutureShouldBePending() {
        LocalDate dueDate = LocalDate.now().plusDays(90);

        ComplianceDeadline deadline = ComplianceDeadline.create(1L, DeadlineType.VISA_EXPIRY, dueDate, "test");

        assertThat(deadline.status()).isEqualTo(DeadlineStatus.PENDING);
        assertThat(deadline.requiresAlert()).isFalse();
    }

    @Test
    @DisplayName("D-30 이내이면 APPROACHING 상태")
    void within30DaysShouldBeApproaching() {
        LocalDate dueDate = LocalDate.now().plusDays(30);

        ComplianceDeadline deadline = ComplianceDeadline.create(1L, DeadlineType.VISA_EXPIRY, dueDate, "test");

        assertThat(deadline.status()).isEqualTo(DeadlineStatus.APPROACHING);
        assertThat(deadline.requiresAlert()).isTrue();
    }

    @Test
    @DisplayName("D-7 이내이면 URGENT 상태")
    void within7DaysShouldBeUrgent() {
        LocalDate dueDate = LocalDate.now().plusDays(7);

        ComplianceDeadline deadline = ComplianceDeadline.create(1L, DeadlineType.VISA_EXPIRY, dueDate, "test");

        assertThat(deadline.status()).isEqualTo(DeadlineStatus.URGENT);
        assertThat(deadline.requiresAlert()).isTrue();
    }

    @Test
    @DisplayName("기한 초과 시 OVERDUE 상태")
    void pastDueShouldBeOverdue() {
        LocalDate dueDate = LocalDate.now().minusDays(1);

        ComplianceDeadline deadline = ComplianceDeadline.create(1L, DeadlineType.VISA_EXPIRY, dueDate, "test");

        assertThat(deadline.status()).isEqualTo(DeadlineStatus.OVERDUE);
        assertThat(deadline.requiresAlert()).isTrue();
    }

    @Test
    @DisplayName("완료 처리 시 COMPLETED 상태")
    void completedShouldBeCompleted() {
        LocalDate dueDate = LocalDate.now().plusDays(7);
        ComplianceDeadline deadline = ComplianceDeadline.create(1L, DeadlineType.VISA_EXPIRY, dueDate, "test");

        deadline.markAsCompleted();

        assertThat(deadline.status()).isEqualTo(DeadlineStatus.COMPLETED);
        assertThat(deadline.requiresAlert()).isFalse();
    }

    @Test
    @DisplayName("날짜 변경 시 상태 자동 재계산")
    void dateChangeShouldRecalculateStatus() {
        ComplianceDeadline deadline = ComplianceDeadline.create(
            1L, DeadlineType.VISA_EXPIRY, LocalDate.now().plusDays(90), "test"
        );
        assertThat(deadline.status()).isEqualTo(DeadlineStatus.PENDING);

        deadline.updateDueDate(LocalDate.now().plusDays(5));

        assertThat(deadline.status()).isEqualTo(DeadlineStatus.URGENT);
    }

}
