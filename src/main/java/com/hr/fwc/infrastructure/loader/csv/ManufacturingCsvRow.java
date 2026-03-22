package com.hr.fwc.infrastructure.loader.csv;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ManufacturingCsvRow {
    @JsonProperty("sub_industry") public String subIndustry;
    @JsonProperty("worker_count") public int workerCount;
    @JsonProperty("year") public int year;
    @JsonProperty("reference_date") public String referenceDate;
}
