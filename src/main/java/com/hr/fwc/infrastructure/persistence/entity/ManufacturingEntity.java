package com.hr.fwc.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "public_data_manufacturing")
public class ManufacturingEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "snapshot_id", nullable = false, length = 50) private String snapshotId;
    @Column(name = "sub_industry", nullable = false, length = 50) private String subIndustry;
    @Column(name = "worker_count", nullable = false) private int workerCount;
    @Column(name = "\"year\"", nullable = false) private int year;
    @Column(name = "reference_date", nullable = false) private LocalDate referenceDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSnapshotId() { return snapshotId; }
    public void setSnapshotId(String snapshotId) { this.snapshotId = snapshotId; }
    public String getSubIndustry() { return subIndustry; }
    public void setSubIndustry(String subIndustry) { this.subIndustry = subIndustry; }
    public int getWorkerCount() { return workerCount; }
    public void setWorkerCount(int workerCount) { this.workerCount = workerCount; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public LocalDate getReferenceDate() { return referenceDate; }
    public void setReferenceDate(LocalDate referenceDate) { this.referenceDate = referenceDate; }
}
