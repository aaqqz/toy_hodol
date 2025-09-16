package com.hodol.toy_hodol.domain.auth.service;

import com.hodol.toy_hodol.common.crypto.PasswordEncoder;
import com.hodol.toy_hodol.common.exception.DuplicateEmailException;
import com.hodol.toy_hodol.domain.auth.entity.User;
import com.hodol.toy_hodol.domain.auth.repository.UserRepository;
import com.hodol.toy_hodol.domain.auth.service.request.SignupServiceRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class AuthServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("회원가입")
    void signup() {
        // given
        SignupServiceRequest request = createSignupServiceRequest();

        // when
        authService.signup(request);

        // then
        assertThat(userRepository.count()).isEqualTo(1L);

        User user = userRepository.findAll().iterator().next();
        assertThat(user.getName()).isEqualTo(request.getName());
        assertThat(user.getEmail()).isEqualTo(request.getEmail());
        assertThat(user.getPassword()).isNotEqualTo(request.getPassword());
        assertThat(passwordEncoder.matches(request.getPassword(), user.getPassword())).isTrue();
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void signupFailWithDuplicatedEmail() {
        // given
        savedUser();

        SignupServiceRequest duplicatedRequest = createSignupServiceRequest();

        // expected
        assertThatThrownBy(() -> authService.signup(duplicatedRequest))
                .isInstanceOf(DuplicateEmailException.class);

        // 여전히 하나의 사용자만 존재
        assertThat(userRepository.count()).isEqualTo(1L);
    }

    private User savedUser() {
        User user = User.builder()
                .name("name")
                .email("test@test.com")
                .password(passwordEncoder.encode("qwer1234"))
                .build();
        userRepository.save(user);

        entityManager.flush();
        entityManager.clear();

        return user;
    }

    private static SignupServiceRequest createSignupServiceRequest() {
        return SignupServiceRequest.builder()
                .email("test@test.com")
                .password("qwer1234")
                .name("name")
                .build();
    }
}