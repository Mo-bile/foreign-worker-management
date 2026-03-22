package com.hr.fwc.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "public_data_regional_industry")
public class RegionalIndustryEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "snapshot_id", nullable = false, length = 50) private String snapshotId;
    @Column(name = "region", nullable = false, length = 30) private String region;
    @Column(name = "industry", nullable = false, length = 30) private String industry;
    @Column(name = "quarter", nullable = false, length = 10) private String quarter;
    @Column(name = "worker_count", nullable = false) private int workerCount;
    @Column(name = "reference_date", nullable = false) private LocalDate referenceDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSnapshotId() { return snapshotId; }
    public void setSnapshotId(String snapshotId) { this.snapshotId = snapshotId; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    public String getQuarter() { return quarter; }
    public void setQuarter(String quarter) { this.quarter = quarter; }
    public int getWorkerCount() { return workerCount; }
    public void setWorkerCount(int workerCount) { this.workerCount = workerCount; }
    public LocalDate getReferenceDate() { return referenceDate; }
    public void setReferenceDate(LocalDate referenceDate) { this.referenceDate = referenceDate; }
}
