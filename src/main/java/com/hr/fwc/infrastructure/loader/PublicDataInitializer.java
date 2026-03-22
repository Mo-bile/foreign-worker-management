package com.hr.fwc.infrastructure.loader;

import com.hr.fwc.domain.publicdata.*;
import com.hr.fwc.infrastructure.loader.csv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class PublicDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(PublicDataInitializer.class);

    private final CsvLoader csvLoader;
    private final RegionalIndustryRepository regionalIndustryRepository;
    private final ManufacturingRepository manufacturingRepository;
    private final VietnamE9Repository vietnamE9Repository;

    public PublicDataInitializer(CsvLoader csvLoader,
                                  RegionalIndustryRepository regionalIndustryRepository,
                                  ManufacturingRepository manufacturingRepository,
                                  VietnamE9Repository vietnamE9Repository) {
        this.csvLoader = csvLoader;
        this.regionalIndustryRepository = regionalIndustryRepository;
        this.manufacturingRepository = manufacturingRepository;
        this.vietnamE9Repository = vietnamE9Repository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        String snapshotId = UUID.randomUUID().toString();
        log.info("공공데이터 적재 시작 (snapshotId={})", snapshotId);

        long start = System.currentTimeMillis();
        loadRegionalIndustry(snapshotId);
        loadManufacturing(snapshotId);
        loadVietnamE9(snapshotId);
        log.info("공공데이터 적재 완료 ({}ms)", System.currentTimeMillis() - start);
    }

    private void loadRegionalIndustry(String snapshotId) {
        List<RegionalIndustryCsvRow> rows = csvLoader.load("data/regional_industry.csv", RegionalIndustryCsvRow.class);
        List<RegionalIndustry> domains = rows.stream()
            .map(r -> RegionalIndustry.create(snapshotId, r.region, r.industry,
                r.quarter, r.workerCount, LocalDate.parse(r.referenceDate)))
            .toList();
        regionalIndustryRepository.saveAll(domains);
        log.info("  지역×업종 현황: {}건 적재", domains.size());
    }

    private void loadManufacturing(String snapshotId) {
        List<ManufacturingCsvRow> rows = csvLoader.load("data/manufacturing.csv", ManufacturingCsvRow.class);
        List<Manufacturing> domains = rows.stream()
            .map(r -> Manufacturing.create(snapshotId, r.subIndustry,
                r.workerCount, r.year, LocalDate.parse(r.referenceDate)))
            .toList();
        manufacturingRepository.saveAll(domains);
        log.info("  제조업 중분류: {}건 적재", domains.size());
    }

    private void loadVietnamE9(String snapshotId) {
        List<VietnamE9CsvRow> rows = csvLoader.load("data/vietnam_e9.csv", VietnamE9CsvRow.class);
        List<VietnamE9> domains = rows.stream()
            .map(r -> VietnamE9.create(snapshotId, r.industry,
                r.totalCount, r.maleCount, r.femaleCount, LocalDate.parse(r.referenceDate)))
            .toList();
        vietnamE9Repository.saveAll(domains);
        log.info("  베트남 E-9: {}건 적재", domains.size());
    }
}
