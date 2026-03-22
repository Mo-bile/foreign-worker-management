package com.hr.fwc.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "quota")
public class QuotaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "\"year\"", nullable = false) private int year;
    @Column(name = "industry", nullable = false, length = 30) private String industry;
    @Column(name = "quota_count", nullable = false) private int quotaCount;
    @Column(name = "source", nullable = false, length = 50) private String source;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    public int getQuotaCount() { return quotaCount; }
    public void setQuotaCount(int quotaCount) { this.quotaCount = quotaCount; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}
