package com.hr.fwc.domain.publicdata;

import java.util.Objects;

public class Quota {

    private Long id;
    private int year;
    private String industry;
    private int quotaCount;
    private String source;

    protected Quota() {}

    private Quota(int year, String industry, int quotaCount, String source) {
        this.industry = Objects.requireNonNull(industry);
        this.source = Objects.requireNonNull(source);
        this.year = year;
        this.quotaCount = quotaCount;
    }

    public static Quota create(int year, String industry, int quotaCount, String source) {
        return new Quota(year, industry, quotaCount, source);
    }

    public static Quota reconstitute(Long id, int year, String industry, int quotaCount, String source) {
        Quota q = new Quota(year, industry, quotaCount, source);
        q.id = id;
        return q;
    }

    public Long id() { return id; }
    public int year() { return year; }
    public String industry() { return industry; }
    public int quotaCount() { return quotaCount; }
    public String source() { return source; }
}
