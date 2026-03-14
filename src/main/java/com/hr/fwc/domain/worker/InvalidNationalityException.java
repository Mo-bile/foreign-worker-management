package com.hr.fwc.domain.worker;

public class InvalidNationalityException extends RuntimeException {
    public InvalidNationalityException(String message) {
        super(message);
    }
}
