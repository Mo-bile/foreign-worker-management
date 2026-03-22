package com.hr.fwc.domain.company;

public class DuplicateBusinessNumberException extends RuntimeException {
    public DuplicateBusinessNumberException(String businessNumber) {
        super("이미 등록된 사업자등록번호입니다: " + businessNumber);
    }
}
