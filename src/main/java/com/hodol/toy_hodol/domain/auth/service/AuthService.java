package com.hodol.toy_hodol.domain.auth.service;

import com.hodol.toy_hodol.common.exception.CustomException;
import com.hodol.toy_hodol.common.exception.ErrorCode;
import com.hodol.toy_hodol.domain.auth.controller.request.SigninRequest;
import com.hodol.toy_hodol.domain.auth.entity.User;
import com.hodol.toy_hodol.domain.auth.repository.UserRepository;
import com.hodol.toy_hodol.domain.auth.service.request.SignupServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 의존성 주입

    @Transactional(readOnly = true)
    public Long signin(SigninRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_SIGNIN_INFORMATION));

        boolean matchesPassword = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!matchesPassword) {
            throw new CustomException(ErrorCode.INVALID_SIGNIN_INFORMATION);
        }

        return user.getId();
    }

    @Transactional
    public void signup(SignupServiceRequest request) {
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {throw new CustomException(ErrorCode.DUPLICATED_EMAIL);});

        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encryptedPassword)
                .build();
        userRepository.save(user);
    }
}
