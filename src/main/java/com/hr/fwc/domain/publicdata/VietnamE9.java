package com.hr.fwc.domain.publicdata;

import java.time.LocalDate;
import java.util.Objects;

public class VietnamE9 {

    private Long id;
    private String snapshotId;
    private String industry;
    private int totalCount;
    private int maleCount;
    private int femaleCount;
    private LocalDate referenceDate;

    protected VietnamE9() {}

    private VietnamE9(String snapshotId, String industry, int totalCount,
                       int maleCount, int femaleCount, LocalDate referenceDate) {
        this.snapshotId = Objects.requireNonNull(snapshotId);
        this.industry = Objects.requireNonNull(industry);
        this.totalCount = totalCount;
        this.maleCount = maleCount;
        this.femaleCount = femaleCount;
        this.referenceDate = Objects.requireNonNull(referenceDate);
    }

    public static VietnamE9 create(String snapshotId, String industry, int totalCount,
                                    int maleCount, int femaleCount, LocalDate referenceDate) {
        return new VietnamE9(snapshotId, industry, totalCount, maleCount, femaleCount, referenceDate);
    }

    public static VietnamE9 reconstitute(Long id, String snapshotId, String industry, int totalCount,
                                          int maleCount, int femaleCount, LocalDate referenceDate) {
        VietnamE9 v = new VietnamE9(snapshotId, industry, totalCount, maleCount, femaleCount, referenceDate);
        v.id = id;
        return v;
    }

    public Long id() { return id; }
    public String snapshotId() { return snapshotId; }
    public String industry() { return industry; }
    public int totalCount() { return totalCount; }
    public int maleCount() { return maleCount; }
    public int femaleCount() { return femaleCount; }
    public LocalDate referenceDate() { return referenceDate; }
}
