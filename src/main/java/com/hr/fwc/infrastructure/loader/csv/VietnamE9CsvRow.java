package com.hr.fwc.infrastructure.loader.csv;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VietnamE9CsvRow {
    @JsonProperty("industry") public String industry;
    @JsonProperty("total_count") public int totalCount;
    @JsonProperty("male_count") public int maleCount;
    @JsonProperty("female_count") public int femaleCount;
    @JsonProperty("reference_date") public String referenceDate;
}
