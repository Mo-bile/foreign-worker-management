package com.hr.fwc.domain.publicdata;

import java.util.List;

public interface ManufacturingRepository {
    List<Manufacturing> saveAll(List<Manufacturing> items);
    List<Manufacturing> findAll();
}
