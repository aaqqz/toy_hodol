package com.hodol.toy_hodol.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {
    private final HttpStatus statusCode;
    private final String message;
//    private final String errorCode;
    private final T data;

 
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(HttpStatus.OK, "", null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK, "", data);
    }

//    public static <T> ApiResponse<T> fail(HttpStatus httpStatus, String message, String errorCode) {
//        return new ApiResponse<>(httpStatus, message, errorCode, null);
//    }

    public static <T> ApiResponse<T> fail(HttpStatus httpStatus, T errorData) {
        return new ApiResponse<>(httpStatus, httpStatus.getReasonPhrase(), errorData);
    }
}
