package com.hr.fwc.domain.worker;

import java.time.LocalDate;
import java.util.Objects;

public class VisaInfo {

    private VisaType visaType;

    private LocalDate visaExpiryDate;

    private LocalDate entryDate;

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
