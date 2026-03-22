package com.hr.fwc.domain.publicdata;

import java.time.LocalDate;
import java.util.Objects;

public class RegionalIndustry {

    private Long id;
    private String snapshotId;
    private String region;
    private String industry;
    private String quarter;
    private int workerCount;
    private LocalDate referenceDate;

    protected RegionalIndustry() {}

    private RegionalIndustry(String snapshotId, String region, String industry,
                              String quarter, int workerCount, LocalDate referenceDate) {
        this.snapshotId = Objects.requireNonNull(snapshotId);
        this.region = Objects.requireNonNull(region);
        this.industry = Objects.requireNonNull(industry);
        this.quarter = Objects.requireNonNull(quarter);
        if (workerCount < 0) {
            throw new IllegalArgumentException("workerCount cannot be negative");
        }
        this.workerCount = workerCount;
        this.referenceDate = Objects.requireNonNull(referenceDate);
    }

    public static RegionalIndustry create(String snapshotId, String region, String industry,
                                           String quarter, int workerCount, LocalDate referenceDate) {
        return new RegionalIndustry(snapshotId, region, industry, quarter, workerCount, referenceDate);
    }

    public static RegionalIndustry reconstitute(Long id, String snapshotId, String region, String industry,
                                                 String quarter, int workerCount, LocalDate referenceDate) {
        RegionalIndustry ri = new RegionalIndustry(snapshotId, region, industry, quarter, workerCount, referenceDate);
        ri.id = id;
        return ri;
    }

    public Long id() { return id; }
    public String snapshotId() { return snapshotId; }
    public String region() { return region; }
    public String industry() { return industry; }
    public String quarter() { return quarter; }
    public int workerCount() { return workerCount; }
    public LocalDate referenceDate() { return referenceDate; }
}
