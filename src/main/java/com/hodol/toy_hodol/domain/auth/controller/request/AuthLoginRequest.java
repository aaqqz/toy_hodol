package com.hodol.toy_hodol.domain.auth.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthLoginRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    private final String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private final String password;

    @Builder
    public AuthLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
