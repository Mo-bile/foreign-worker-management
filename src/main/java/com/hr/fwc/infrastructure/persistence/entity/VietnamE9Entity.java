package com.hr.fwc.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "public_data_vietnam_e9")
public class VietnamE9Entity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "snapshot_id", nullable = false, length = 50) private String snapshotId;
    @Column(name = "industry", nullable = false, length = 30) private String industry;
    @Column(name = "total_count", nullable = false) private int totalCount;
    @Column(name = "male_count", nullable = false) private int maleCount;
    @Column(name = "female_count", nullable = false) private int femaleCount;
    @Column(name = "reference_date", nullable = false) private LocalDate referenceDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSnapshotId() { return snapshotId; }
    public void setSnapshotId(String snapshotId) { this.snapshotId = snapshotId; }
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    public int getTotalCount() { return totalCount; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
    public int getMaleCount() { return maleCount; }
    public void setMaleCount(int maleCount) { this.maleCount = maleCount; }
    public int getFemaleCount() { return femaleCount; }
    public void setFemaleCount(int femaleCount) { this.femaleCount = femaleCount; }
    public LocalDate getReferenceDate() { return referenceDate; }
    public void setReferenceDate(LocalDate referenceDate) { this.referenceDate = referenceDate; }
}
