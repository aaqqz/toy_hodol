package com.hodol.toy_hodol.domain.auth.service;

import com.hodol.toy_hodol.common.crypto.PasswordEncoder;
import com.hodol.toy_hodol.common.exception.DuplicateEmailException;
import com.hodol.toy_hodol.common.exception.InvalidSigninException;
import com.hodol.toy_hodol.domain.auth.controller.request.SigninRequest;
import com.hodol.toy_hodol.domain.auth.entity.User;
import com.hodol.toy_hodol.domain.auth.repository.UserRepository;
import com.hodol.toy_hodol.domain.auth.service.request.SignupServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Long signin(SigninRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidSigninException("로그인 정보가 올바르지 않습니다."));

        boolean matchesPassword = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!matchesPassword) {
            throw new InvalidSigninException("로그인 정보가 올바르지 않습니다.");
        }

        return user.getId();
    }

    @Transactional
    public void signup(SignupServiceRequest request) {
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {throw new DuplicateEmailException("이미 존재하는 이메일입니다.");});

        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encryptedPassword)
                .build();
        userRepository.save(user);
    }
}
