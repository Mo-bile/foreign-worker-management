package com.hr.fwc.domain.company;

public enum IndustryCategory {
    MANUFACTURING("제조업"),
    CONSTRUCTION("건설업"),
    AGRICULTURE("농축산업"),
    FISHING("어업"),
    SERVICE("서비스업"),
    MINING("광업"),
    TRANSPORTATION("운수업"),
    ACCOMMODATION("숙박음식점업");

    private final String koreanName;

    IndustryCategory(String koreanName) {
        this.koreanName = koreanName;
    }

    public String koreanName() {
        return koreanName;
    }
}
