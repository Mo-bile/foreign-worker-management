package com.hr.fwc.domain.worker;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "foreign_workers")
public class ForeignWorker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private PersonalInfo personalInfo;

    @Embedded
    private VisaInfo visaInfo;

    @Embedded
    private EmploymentInfo employmentInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "nationality", nullable = false, length = 10)
    private Nationality nationality;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected ForeignWorker() {
    }

    private ForeignWorker(PersonalInfo personalInfo, VisaInfo visaInfo, 
                         EmploymentInfo employmentInfo, Nationality nationality) {
        this.personalInfo = Objects.requireNonNull(personalInfo, "Personal info cannot be null");
        this.visaInfo = Objects.requireNonNull(visaInfo, "Visa info cannot be null");
        this.employmentInfo = Objects.requireNonNull(employmentInfo, "Employment info cannot be null");
        this.nationality = Objects.requireNonNull(nationality, "Nationality cannot be null");
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static ForeignWorker create(PersonalInfo personalInfo, VisaInfo visaInfo,
                                      EmploymentInfo employmentInfo, Nationality nationality) {
        return new ForeignWorker(personalInfo, visaInfo, employmentInfo, nationality);
    }

    public void updateVisaInfo(VisaInfo newVisaInfo) {
        this.visaInfo = Objects.requireNonNull(newVisaInfo, "Visa info cannot be null");
        this.updatedAt = LocalDateTime.now();
    }

    public void updateEmploymentInfo(EmploymentInfo newEmploymentInfo) {
        this.employmentInfo = Objects.requireNonNull(newEmploymentInfo, "Employment info cannot be null");
        this.updatedAt = LocalDateTime.now();
    }

    public boolean hasSocialSecurityAgreement() {
        return nationality.hasSocialSecurityAgreement();
    }

    public Long id() {
        return id;
    }

    public PersonalInfo personalInfo() {
        return personalInfo;
    }

    public VisaInfo visaInfo() {
        return visaInfo;
    }

    public EmploymentInfo employmentInfo() {
        return employmentInfo;
    }

    public Nationality nationality() {
        return nationality;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public LocalDateTime updatedAt() {
        return updatedAt;
    }

}
