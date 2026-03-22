package com.hr.fwc.domain.publicdata;

import java.util.List;

public interface RegionalIndustryRepository {
    List<RegionalIndustry> saveAll(List<RegionalIndustry> items);
    List<RegionalIndustry> findAll();
}
