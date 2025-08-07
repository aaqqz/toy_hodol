package com.hodol.toy_hodol.domain.auth.controller;

import com.hodol.toy_hodol.common.response.ApiResponse;
import com.hodol.toy_hodol.domain.auth.controller.request.AuthLoginRequest;
import com.hodol.toy_hodol.domain.auth.service.AuthService;
import com.hodol.toy_hodol.domain.auth.service.response.SessionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Object> signin(@RequestBody @Valid AuthLoginRequest request) {
        SessionResponse session = authService.signin(request);
        ResponseCookie cookie = ResponseCookie.from("accessToken", session.getAccessToken())
                .domain("localhost") // todo 서버 환경에 따른 분리 필요
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(30))
                .sameSite("Strict")
                .build();

        log.info("########## {}", cookie.toString());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
