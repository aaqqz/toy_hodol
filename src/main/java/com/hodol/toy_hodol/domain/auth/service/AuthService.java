package com.hodol.toy_hodol.domain.auth.service;

import com.hodol.toy_hodol.common.crypto.PasswordEncoder;
import com.hodol.toy_hodol.common.exception.DuplicateEmailException;
import com.hodol.toy_hodol.domain.auth.entity.User;
import com.hodol.toy_hodol.domain.auth.repository.UserRepository;
import com.hodol.toy_hodol.domain.auth.service.request.SignupServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupServiceRequest request) {
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {throw new DuplicateEmailException("이미 존재하는 이메일입니다.");});

        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encryptedPassword)
                .build();
        userRepository.save(user);
    }
}
