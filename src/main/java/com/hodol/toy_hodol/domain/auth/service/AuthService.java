package com.hodol.toy_hodol.domain.auth.service;

import com.hodol.toy_hodol.common.exception.CustomException;
import com.hodol.toy_hodol.common.exception.ErrorCode;
import com.hodol.toy_hodol.domain.auth.controller.request.AuthLoginRequest;
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

    @Transactional(readOnly = true)
    public Long signin(AuthLoginRequest request) {
        User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_SIGNIN_INFORMATION));

        return user.getId();
    }

    @Transactional
    public void signup(SignupServiceRequest request) {
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {throw new CustomException(ErrorCode.DUPLICATED_EMAIL);});

        var user = User.signup(request);
        userRepository.save(user);
    }
}
