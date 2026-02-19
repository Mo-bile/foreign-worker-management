package com.hr.fwc.domain.worker;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class VisaInfo {

    @Enumerated(EnumType.STRING)
    @Column(name = "visa_type", nullable = false, length = 10)
    private VisaType visaType;

    @Column(name = "visa_expiry_date", nullable = false)
    private LocalDate visaExpiryDate;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "registration_number", length = 20)
    private String registrationNumber;

    protected VisaInfo() {
    }

    private VisaInfo(VisaType visaType, LocalDate visaExpiryDate, LocalDate entryDate, String registrationNumber) {
        this.visaType = Objects.requireNonNull(visaType, "Visa type cannot be null");
        this.visaExpiryDate = Objects.requireNonNull(visaExpiryDate, "Visa expiry date cannot be null");
        this.entryDate = Objects.requireNonNull(entryDate, "Entry date cannot be null");
        this.registrationNumber = registrationNumber;
    }

    public static VisaInfo of(VisaType visaType, LocalDate visaExpiryDate, LocalDate entryDate, String registrationNumber) {
        return new VisaInfo(visaType, visaExpiryDate, entryDate, registrationNumber);
    }

    public boolean isExpiringWithinDays(int days) {
        return LocalDate.now().plusDays(days).isAfter(visaExpiryDate) || LocalDate.now().plusDays(days).isEqual(visaExpiryDate);
    }

    public int daysUntilExpiry() {
        return (int) java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), visaExpiryDate);
    }

    public VisaType visaType() {
        return visaType;
    }

    public LocalDate visaExpiryDate() {
        return visaExpiryDate;
    }

    public LocalDate entryDate() {
        return entryDate;
    }

    public String registrationNumber() {
        return registrationNumber;
    }

}
