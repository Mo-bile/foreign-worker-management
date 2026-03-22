package com.hr.fwc.infrastructure.loader;

import com.hr.fwc.domain.publicdata.ManufacturingRepository;
import com.hr.fwc.domain.publicdata.RegionalIndustryRepository;
import com.hr.fwc.domain.publicdata.VietnamE9Repository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PublicDataInitializerIntegrationTest {

    @Autowired
    private RegionalIndustryRepository regionalIndustryRepository;

    @Autowired
    private ManufacturingRepository manufacturingRepository;

    @Autowired
    private VietnamE9Repository vietnamE9Repository;

    @Test
    @DisplayName("앱 시작 시 CSV 데이터가 자동 적재된다")
    void CSV_자동_적재_확인() {
        assertThat(regionalIndustryRepository.findAll()).hasSize(5);
        assertThat(manufacturingRepository.findAll()).hasSize(5);
        assertThat(vietnamE9Repository.findAll()).hasSize(5);
    }
}
