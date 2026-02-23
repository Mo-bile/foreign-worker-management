package com.hr.fwc.infrastructure.persistence.entity;

import com.hr.fwc.domain.worker.Nationality;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "foreign_workers")
public class ForeignWorkerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private PersonalInfoEmbeddable personalInfo;

    @Embedded
    private VisaInfoEmbeddable visaInfo;

    @Embedded
    private EmploymentInfoEmbeddable employmentInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "nationality", nullable = false, length = 10)
    private Nationality nationality;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PersonalInfoEmbeddable getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInfoEmbeddable personalInfo) {
        this.personalInfo = personalInfo;
    }

    public VisaInfoEmbeddable getVisaInfo() {
        return visaInfo;
    }

    public void setVisaInfo(VisaInfoEmbeddable visaInfo) {
        this.visaInfo = visaInfo;
    }

    public EmploymentInfoEmbeddable getEmploymentInfo() {
        return employmentInfo;
    }

    public void setEmploymentInfo(EmploymentInfoEmbeddable employmentInfo) {
        this.employmentInfo = employmentInfo;
    }

    public Nationality getNationality() {
        return nationality;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
