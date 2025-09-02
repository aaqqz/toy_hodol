package com.hodol.toy_hodol.domain.auth.controller;

import com.hodol.toy_hodol.common.response.ApiResponse;
import com.hodol.toy_hodol.config.AppConfig;
import com.hodol.toy_hodol.domain.auth.controller.request.AuthLoginRequest;
import com.hodol.toy_hodol.domain.auth.controller.request.SignupRequest;
import com.hodol.toy_hodol.domain.auth.service.AuthService;
import com.hodol.toy_hodol.domain.auth.service.response.SessionResponse;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AppConfig appConfig;

    @PostMapping("/login")
    public ApiResponse<SessionResponse> signin(@RequestBody @Valid AuthLoginRequest request) {
        Long userId = authService.signin(request);

        SecretKey secretKey = appConfig.getSecretKey();
        String jws = Jwts.builder()
                .subject(userId.toString())
                .issuedAt(new Date())
                .signWith(secretKey)
                .compact();

        return ApiResponse.success(new SessionResponse(jws));
    }

    @PostMapping("/signup")
    public ApiResponse<Void> signup(@RequestBody SignupRequest request) {
        authService.signup(request.toServiceRequest());
        return ApiResponse.success();
    }

}
