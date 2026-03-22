package com.hr.fwc.presentation;

import com.hr.fwc.domain.company.CompanyNotFoundException;
import com.hr.fwc.domain.company.DuplicateBusinessNumberException;
import com.hr.fwc.domain.worker.InvalidNationalityException;
import com.hr.fwc.domain.worker.InvalidVisaTypeException;
import com.hr.fwc.domain.worker.WorkerNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidVisaTypeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidVisaType(InvalidVisaTypeException ex) {
        log.warn("잘못된 비자 유형 요청: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(InvalidNationalityException.class)
    public ResponseEntity<ErrorResponse> handleInvalidNationality(InvalidNationalityException ex) {
        log.warn("잘못된 국적 코드 요청: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(WorkerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleWorkerNotFound(WorkerNotFoundException ex) {
        log.warn("근로자 조회 실패: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCompanyNotFound(CompanyNotFoundException ex) {
        log.warn("사업장 조회 실패: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DuplicateBusinessNumberException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateBusinessNumber(DuplicateBusinessNumberException ex) {
        log.warn("중복 사업자등록번호: {}", ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("요청 본문 파싱 실패: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "요청 데이터 형식이 올바르지 않습니다");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
            .map(v -> v.getPropertyPath() + ": " + v.getMessage())
            .reduce((a, b) -> a + ", " + b)
            .orElse("제약 조건 위반");
        log.warn("제약 조건 위반: {}", message);
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("파라미터 타입 불일치: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST,
            String.format("'%s' 파라미터의 값이 올바르지 않습니다", ex.getName()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .reduce((a, b) -> a + ", " + b)
            .orElse("유효성 검증 실패");
        log.warn("유효성 검증 실패: {}", message);
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    // Catch-all for unexpected exceptions. Returns generic 500 to avoid leaking internal details.
    // Must remain the last handler so more specific handlers take precedence.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("서버 내부 오류 발생", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다");
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status)
            .body(ErrorResponse.of(status.value(), status.getReasonPhrase(), message));
    }
}
