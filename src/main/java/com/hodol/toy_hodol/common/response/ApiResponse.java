package com.hodol.toy_hodol.common.response;

import com.hodol.toy_hodol.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {
    private final HttpStatus statusCode;
    private final String message;
    private final String errorCode;
    private final T data;

 
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(HttpStatus.OK, "", "", null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK, "", "", data);
    }

    public static <T> ApiResponse<T> fail(ErrorCode errorCode, T errorData) {
        return new ApiResponse<>(errorCode.getHttpStatus(), errorCode.getMessage(), errorCode.name(), errorData);
    }

    public static <T> ApiResponse<T> fail(HttpStatus httpStatus, T errorData) {
        return new ApiResponse<>(httpStatus, httpStatus.getReasonPhrase(), httpStatus.name(), errorData);
    }
}
