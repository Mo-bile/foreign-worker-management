package com.hr.fwc.domain.publicdata;

import java.util.List;

public interface QuotaRepository {
    List<Quota> findAll();
    List<Quota> findByYear(int year);
}
