package com.hr.fwc.infrastructure.persistence.entity;

import com.hr.fwc.domain.worker.VisaType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

@Embeddable
public class VisaInfoEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "visa_type", nullable = false, length = 10)
    private VisaType visaType;

    @Column(name = "visa_expiry_date", nullable = false)
    private LocalDate visaExpiryDate;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "registration_number", length = 20)
    private String registrationNumber;

    public VisaType getVisaType() {
        return visaType;
    }

    public void setVisaType(VisaType visaType) {
        this.visaType = visaType;
    }

    public LocalDate getVisaExpiryDate() {
        return visaExpiryDate;
    }

    public void setVisaExpiryDate(LocalDate visaExpiryDate) {
        this.visaExpiryDate = visaExpiryDate;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
}
