package com.hr.fwc.domain.compliance;

public enum DeadlineType {
    VISA_EXPIRY("비자만료", "비자 갱신 필요", 90, 30, 7),
    INSURANCE_ENROLLMENT("보험가입", "4대보험 가입 기한", 30, 14, 3),
    CHANGE_REPORT("고용변동신고", "고용센터 신고 의무", 15, 10, 3),
    CONTRACT_RENEWAL("근로계약갱신", "계약 만료 및 갱신", 30, 14, 7);

    private final String koreanName;
    private final String description;
    private final int alertDays90;
    private final int alertDays30;
    private final int alertDays7;

    DeadlineType(String koreanName, String description, int alertDays90, int alertDays30, int alertDays7) {
        this.koreanName = koreanName;
        this.description = description;
        this.alertDays90 = alertDays90;
        this.alertDays30 = alertDays30;
        this.alertDays7 = alertDays7;
    }

    public String koreanName() {
        return koreanName;
    }

    public String description() {
        return description;
    }

    public int[] alertThresholds() {
        return new int[]{alertDays90, alertDays30, alertDays7};
    }

}
