package com.hr.fwc.domain.company;

public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException(Long id) {
        super("사업장을 찾을 수 없습니다: ID=" + id);
    }
}
