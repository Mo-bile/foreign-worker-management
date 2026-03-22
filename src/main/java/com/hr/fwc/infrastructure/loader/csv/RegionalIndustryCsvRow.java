package com.hr.fwc.infrastructure.loader.csv;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegionalIndustryCsvRow {
    @JsonProperty("region") public String region;
    @JsonProperty("industry") public String industry;
    @JsonProperty("quarter") public String quarter;
    @JsonProperty("worker_count") public int workerCount;
    @JsonProperty("reference_date") public String referenceDate;
}
