package com.hr.fwc.domain.worker;

public enum VisaType {
    E9("고용허가제 일반외국인"),
    H2("외국국적동포"),
    E7("특정활동 전문직"),
    E7_4("숙련기능인력"),
    F2("거주"),
    F5("영주"),
    F6("결혼이민");

    private final String description;

    VisaType(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

}
