package com.hodol.toy_hodol.domain.auth.service.request;

import com.hodol.toy_hodol.domain.auth.controller.request.SignupRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignupServiceRequest {

    private final String email;
    private final String password;
    private final String name;

    @Builder
    private SignupServiceRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static SignupServiceRequest of(SignupRequest request) {
        return SignupServiceRequest.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .build();
    }
}
