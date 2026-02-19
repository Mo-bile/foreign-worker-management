package com.hr.fwc.domain.insurance;

public enum InsuranceType {
    NATIONAL_PENSION("국민연금", "국민연금공단"),
    HEALTH_INSURANCE("건강보험", "건강보험공단"),
    EMPLOYMENT_INSURANCE("고용보험", "근로복지공단"),
    INDUSTRIAL_ACCIDENT("산재보험", "근로복지공단");

    private final String koreanName;
    private final String administeringAgency;

    InsuranceType(String koreanName, String administeringAgency) {
        this.koreanName = koreanName;
        this.administeringAgency = administeringAgency;
    }

    public String koreanName() {
        return koreanName;
    }

    public String administeringAgency() {
        return administeringAgency;
    }

}
