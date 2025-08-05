package com.hodol.toy_hodol.domain.user.controller;

import com.hodol.toy_hodol.domain.user.controller.request.AuthLoginRequest;
import com.hodol.toy_hodol.domain.user.repository.UserRepository;
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

    private final UserRepository userRepository;

    // todo login 작업중
    @PostMapping("/login")
    public String login(@RequestBody @Valid AuthLoginRequest request) {
        log.info("##### = {}, {}", request.getEmail(), request.getPassword());
        return "로그인 성공";
    }
}
