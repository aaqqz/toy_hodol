package com.hodol.toy_hodol.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception e) {
        log.error("API Exception: {}", e.getMessage(), e);

        return getProblemDetail(INTERNAL_SERVER_ERROR , e);
    }

    @ExceptionHandler({PostNotFoundException.class})
    public ProblemDetail notFoundHandleException(Exception e) {
        log.error("API Exception: {}", e.getMessage(), e);

        return getProblemDetail(NOT_FOUND , e);
    }

    @ExceptionHandler(BindException.class)
    public ProblemDetail invalidRequestHandler(BindException e) {
        log.error("API Exception: {}", e.getMessage(), e);

        // 모든 필드 오류를 field -> message 형태의 맵으로 구성
        Map<String, String> errors = new java.util.LinkedHashMap<>();
        for (FieldError fe : e.getBindingResult().getFieldErrors()) {
            // 같은 필드에 여러 메시지가 있어도 첫 번째만 채택
            errors.putIfAbsent(fe.getField(), fe.getDefaultMessage());
        }

        ProblemDetail problemDetail;

        if (errors.size() <= 1) {
            // 단일 오류일 때: detail 을 문자열로
            String message = errors.isEmpty()
                    ? "잘못된 요청입니다."
                    : (errors.containsKey("content")
                    ? errors.get("content")
                    : errors.values().iterator().next());

            problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, message);
        } else {
            // 복수 오류일 때: detail 을 객체(맵)로
            problemDetail = ProblemDetail.forStatus(BAD_REQUEST);
            problemDetail.setProperty("detail", errors);
        }

        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("exception", e.getClass().getSimpleName());
        return problemDetail;
    }

    private static ProblemDetail getProblemDetail(HttpStatus status, Exception e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, e.getMessage());

        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("exception", e.getClass().getSimpleName());
        return problemDetail;
    }
}
