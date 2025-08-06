package com.hodol.toy_hodol.domain.auth.controller;

import com.hodol.toy_hodol.common.response.ApiResponse;
import com.hodol.toy_hodol.domain.auth.controller.request.AuthLoginRequest;
import com.hodol.toy_hodol.domain.auth.service.AuthService;
import com.hodol.toy_hodol.domain.auth.service.response.SessionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<SessionResponse> signin(@RequestBody @Valid AuthLoginRequest request) {
        return ApiResponse.success(authService.signin(request));
    }
}
