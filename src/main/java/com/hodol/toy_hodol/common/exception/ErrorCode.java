package com.hodol.toy_hodol.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 글입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
