package com.hodol.toy_hodol.domain.auth.service;

import com.hodol.toy_hodol.common.exception.CustomException;
import com.hodol.toy_hodol.common.exception.ErrorCode;
import com.hodol.toy_hodol.domain.auth.controller.request.AuthLoginRequest;
import com.hodol.toy_hodol.domain.auth.entity.Session;
import com.hodol.toy_hodol.domain.auth.entity.User;
import com.hodol.toy_hodol.domain.auth.repository.UserRepository;
import com.hodol.toy_hodol.domain.auth.service.response.SessionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public SessionResponse signin(AuthLoginRequest request) {
        User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_SIGNIN_INFORMATION));

        Session session = user.addSession();

        return new SessionResponse(session.getAccessToken());
    }
}
