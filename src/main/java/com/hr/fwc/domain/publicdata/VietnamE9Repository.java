package com.hr.fwc.domain.publicdata;

import java.util.List;

public interface VietnamE9Repository {
    List<VietnamE9> saveAll(List<VietnamE9> items);
    List<VietnamE9> findAll();
}
