package com.hr.fwc.presentation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record ErrorResponse(
    int status,
    String error,
    String message,
    String timestamp
) {

    public static ErrorResponse of(int status, String error, String message) {
        return new ErrorResponse(
            status,
            error,
            message,
            LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }
}
