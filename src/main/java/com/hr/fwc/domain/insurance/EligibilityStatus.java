package com.hr.fwc.domain.insurance;

public enum EligibilityStatus {
    MANDATORY("의무가입", "가입 필수"),
    OPTIONAL("임의가입", "선택적 가입 가능"),
    EXEMPT("가입제외", "가입 의무 없음");

    private final String koreanName;
    private final String description;

    EligibilityStatus(String koreanName, String description) {
        this.koreanName = koreanName;
        this.description = description;
    }

    public String koreanName() {
        return koreanName;
    }

    public String description() {
        return description;
    }

}
