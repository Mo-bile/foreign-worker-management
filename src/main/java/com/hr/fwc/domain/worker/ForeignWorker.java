package com.hr.fwc.domain.worker;

import java.time.LocalDateTime;
import java.util.Objects;

public class ForeignWorker {

    private Long id;

    private PersonalInfo personalInfo;

    private VisaInfo visaInfo;

    private EmploymentInfo employmentInfo;

    private Nationality nationality;

    private LocalDateTime createdAt;

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

    public static ForeignWorker reconstitute(Long id, PersonalInfo personalInfo, VisaInfo visaInfo,
                                             EmploymentInfo employmentInfo, Nationality nationality,
                                             LocalDateTime createdAt, LocalDateTime updatedAt) {
        ForeignWorker w = new ForeignWorker(personalInfo, visaInfo, employmentInfo, nationality);
        w.id = id;
        w.createdAt = createdAt;
        w.updatedAt = updatedAt;
        return w;
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
