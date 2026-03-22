package com.hr.fwc.infrastructure.loader.csv;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegionalIndustryCsvRow {
    @JsonProperty("region") private String region;
    @JsonProperty("industry") private String industry;
    @JsonProperty("quarter") private String quarter;
    @JsonProperty("worker_count") private int workerCount;
    @JsonProperty("reference_date") private String referenceDate;

    public String getRegion() { return region; }
    public String getIndustry() { return industry; }
    public String getQuarter() { return quarter; }
    public int getWorkerCount() { return workerCount; }
    public String getReferenceDate() { return referenceDate; }
}
