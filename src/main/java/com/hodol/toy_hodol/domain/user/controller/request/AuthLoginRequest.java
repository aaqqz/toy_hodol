package com.hodol.toy_hodol.domain.user.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthLoginRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    private final String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private final String password;
}
