package com.hr.fwc.infrastructure.loader.csv;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VietnamE9CsvRow {
    @JsonProperty("industry") private String industry;
    @JsonProperty("total_count") private int totalCount;
    @JsonProperty("male_count") private int maleCount;
    @JsonProperty("female_count") private int femaleCount;
    @JsonProperty("reference_date") private String referenceDate;

    public String getIndustry() { return industry; }
    public int getTotalCount() { return totalCount; }
    public int getMaleCount() { return maleCount; }
    public int getFemaleCount() { return femaleCount; }
    public String getReferenceDate() { return referenceDate; }
}
