package com.hr.fwc.application.service;

import com.hr.fwc.domain.compliance.ComplianceDeadline;
import com.hr.fwc.domain.compliance.ComplianceDeadlineRepository;
import com.hr.fwc.domain.compliance.DeadlineStatus;
import com.hr.fwc.domain.compliance.DeadlineType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ComplianceDashboardServiceIntegrationTest {

    @Autowired
    private ComplianceDashboardService dashboardService;

    @Autowired
    private ComplianceDeadlineRepository deadlineRepository;

    @BeforeEach
    void setUp() {
        createDeadline(1L, DeadlineType.VISA_EXPIRY, LocalDate.now().minusDays(5), DeadlineStatus.OVERDUE);
        createDeadline(2L, DeadlineType.VISA_EXPIRY, LocalDate.now().plusDays(5), DeadlineStatus.URGENT);
        createDeadline(3L, DeadlineType.INSURANCE_ENROLLMENT, LocalDate.now().plusDays(20), DeadlineStatus.APPROACHING);
        createDeadline(4L, DeadlineType.VISA_EXPIRY, LocalDate.now().plusDays(100), DeadlineStatus.PENDING);
        createDeadline(5L, DeadlineType.VISA_EXPIRY, LocalDate.now().plusDays(3), DeadlineStatus.COMPLETED);
    }

    @Test
    @DisplayName("기한 초과 데드라인 조회")
    void getOverdueDeadlines() {
        List<ComplianceDeadline> overdue = dashboardService.getOverdueDeadlines();

        assertThat(overdue).hasSize(1);
        assertThat(overdue.get(0).workerId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("임박한 데드라인 조회 (30일 이내)")
    void getUpcomingDeadlines() {
        List<ComplianceDeadline> upcoming = dashboardService.getUpcomingDeadlines(30);

        assertThat(upcoming).hasSize(3);
        assertThat(upcoming)
            .extracting(ComplianceDeadline::workerId)
            .containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    @DisplayName("특정 근로자의 데드라인 조회")
    void getWorkerDeadlines() {
        createDeadline(10L, DeadlineType.VISA_EXPIRY, LocalDate.now().plusDays(10), DeadlineStatus.URGENT);
        createDeadline(10L, DeadlineType.INSURANCE_ENROLLMENT, LocalDate.now().plusDays(25), DeadlineStatus.APPROACHING);

        List<ComplianceDeadline> workerDeadlines = dashboardService.getWorkerDeadlines(10L);

        assertThat(workerDeadlines).hasSize(2);
    }

    private void createDeadline(Long workerId, DeadlineType type, LocalDate dueDate, DeadlineStatus status) {
        ComplianceDeadline deadline = ComplianceDeadline.create(workerId, type, dueDate, "test");
        if (status == DeadlineStatus.COMPLETED) {
            deadline.markAsCompleted();
        }
        deadlineRepository.save(deadline);
    }

}
