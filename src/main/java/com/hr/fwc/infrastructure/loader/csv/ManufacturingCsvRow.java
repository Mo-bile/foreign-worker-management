package com.hr.fwc.infrastructure.loader.csv;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ManufacturingCsvRow {
    @JsonProperty("sub_industry") private String subIndustry;
    @JsonProperty("worker_count") private int workerCount;
    @JsonProperty("year") private int year;
    @JsonProperty("reference_date") private String referenceDate;

    public String getSubIndustry() { return subIndustry; }
    public int getWorkerCount() { return workerCount; }
    public int getYear() { return year; }
    public String getReferenceDate() { return referenceDate; }
}
