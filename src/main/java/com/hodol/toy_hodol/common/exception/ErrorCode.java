package com.hodol.toy_hodol.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 글입니다."),

    INVALID_SIGNIN_INFORMATION(HttpStatus.UNAUTHORIZED, "로그인 정보가 올바르지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
