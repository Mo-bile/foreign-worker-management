package com.hr.fwc.domain.compliance;

public enum DeadlineStatus {
    PENDING("예정"),
    APPROACHING("임박"),
    URGENT("긴급"),
    OVERDUE("기한초과"),
    COMPLETED("완료");

    private final String koreanName;

    DeadlineStatus(String koreanName) {
        this.koreanName = koreanName;
    }

    public String koreanName() {
        return koreanName;
    }

}
