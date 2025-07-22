package com.hodol.toy_hodol.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    private String fieldName;
    private String message;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, String fieldName, String message) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.fieldName = fieldName;
        this.message = message;
    }
}
