package com.hr.fwc.domain.publicdata;

import java.time.LocalDate;
import java.util.Objects;

public class Manufacturing {

    private Long id;
    private String snapshotId;
    private String subIndustry;
    private int workerCount;
    private int year;
    private LocalDate referenceDate;

    protected Manufacturing() {}

    private Manufacturing(String snapshotId, String subIndustry, int workerCount,
                           int year, LocalDate referenceDate) {
        this.snapshotId = Objects.requireNonNull(snapshotId);
        this.subIndustry = Objects.requireNonNull(subIndustry);
        if (workerCount < 0) {
            throw new IllegalArgumentException("workerCount cannot be negative");
        }
        if (year < 1) {
            throw new IllegalArgumentException("year must be positive");
        }
        this.workerCount = workerCount;
        this.year = year;
        this.referenceDate = Objects.requireNonNull(referenceDate);
    }

    public static Manufacturing create(String snapshotId, String subIndustry, int workerCount,
                                        int year, LocalDate referenceDate) {
        return new Manufacturing(snapshotId, subIndustry, workerCount, year, referenceDate);
    }

    public static Manufacturing reconstitute(Long id, String snapshotId, String subIndustry,
                                              int workerCount, int year, LocalDate referenceDate) {
        Manufacturing m = new Manufacturing(snapshotId, subIndustry, workerCount, year, referenceDate);
        m.id = id;
        return m;
    }

    public Long id() { return id; }
    public String snapshotId() { return snapshotId; }
    public String subIndustry() { return subIndustry; }
    public int workerCount() { return workerCount; }
    public int year() { return year; }
    public LocalDate referenceDate() { return referenceDate; }
}
