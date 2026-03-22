package com.hr.fwc.domain.publicdata;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

class PublicDataDomainTest {

    @Test
    @DisplayName("RegionalIndustry 생성 시 snapshotId와 필드가 올바르게 설정됨")
    void RegionalIndustry_생성() {
        RegionalIndustry ri = RegionalIndustry.create(
            "snap-1", "서울특별시", "제조업", "2025Q1", 12500, LocalDate.of(2025, 3, 31)
        );
        assertThat(ri.snapshotId()).isEqualTo("snap-1");
        assertThat(ri.region()).isEqualTo("서울특별시");
        assertThat(ri.industry()).isEqualTo("제조업");
        assertThat(ri.quarter()).isEqualTo("2025Q1");
        assertThat(ri.workerCount()).isEqualTo(12500);
        assertThat(ri.referenceDate()).isEqualTo(LocalDate.of(2025, 3, 31));
    }

    @Test
    @DisplayName("Manufacturing 생성 시 snapshotId와 필드가 올바르게 설정됨")
    void Manufacturing_생성() {
        Manufacturing m = Manufacturing.create(
            "snap-1", "식료품제조업", 8500, 2024, LocalDate.of(2024, 12, 31)
        );
        assertThat(m.snapshotId()).isEqualTo("snap-1");
        assertThat(m.subIndustry()).isEqualTo("식료품제조업");
        assertThat(m.workerCount()).isEqualTo(8500);
        assertThat(m.year()).isEqualTo(2024);
    }

    @Test
    @DisplayName("VietnamE9 생성 시 snapshotId와 필드가 올바르게 설정됨")
    void VietnamE9_생성() {
        VietnamE9 v = VietnamE9.create(
            "snap-1", "제조업", 45000, 32000, 13000, LocalDate.of(2024, 12, 31)
        );
        assertThat(v.snapshotId()).isEqualTo("snap-1");
        assertThat(v.totalCount()).isEqualTo(45000);
        assertThat(v.maleCount()).isEqualTo(32000);
        assertThat(v.femaleCount()).isEqualTo(13000);
    }

    @Test
    @DisplayName("Quota 생성 시 snapshotId 없이 필드가 올바르게 설정됨")
    void Quota_생성() {
        Quota q = Quota.create(2025, "제조업", 36000, "도입계획");
        assertThat(q.year()).isEqualTo(2025);
        assertThat(q.industry()).isEqualTo("제조업");
        assertThat(q.quotaCount()).isEqualTo(36000);
        assertThat(q.source()).isEqualTo("도입계획");
    }
}
