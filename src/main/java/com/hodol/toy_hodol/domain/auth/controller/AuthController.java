package com.hodol.toy_hodol.domain.auth.controller;

import com.hodol.toy_hodol.common.response.ApiResponse;
import com.hodol.toy_hodol.domain.auth.controller.request.AuthLoginRequest;
import com.hodol.toy_hodol.domain.auth.service.AuthService;
import com.hodol.toy_hodol.domain.auth.service.response.SessionResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private static final String KEY = "+uXZZBWa5RGr+ATqTRPgYFladpxz6ndXPeOoxL+rPTI=";

    @PostMapping("/login")
    public ApiResponse<SessionResponse> signin(@RequestBody @Valid AuthLoginRequest request) {
        Long userId = authService.signin(request);

        SecretKey secretKey = Keys.hmacShaKeyFor(KEY.getBytes(StandardCharsets.UTF_8));
        String jws = Jwts.builder()
                .subject(userId.toString())
                .signWith(secretKey)
                .compact();

        return ApiResponse.success(new SessionResponse(jws));
    }
}
