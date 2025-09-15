package com.hodol.toy_hodol.domain.auth.service;

import com.hodol.toy_hodol.common.crypto.PasswordEncoder;
import com.hodol.toy_hodol.common.crypto.SecurePasswordEncoder;
import com.hodol.toy_hodol.common.exception.CustomException;
import com.hodol.toy_hodol.common.exception.ErrorCode;
import com.hodol.toy_hodol.domain.auth.controller.request.SigninRequest;
import com.hodol.toy_hodol.domain.auth.entity.User;
import com.hodol.toy_hodol.domain.auth.repository.UserRepository;
import com.hodol.toy_hodol.domain.auth.service.request.SignupServiceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void clean() {
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("로그인 성공")
    void signin() {
        // given
        savedUser();

        // given
//        SignupServiceRequest signupRequest = createSignupServiceRequest();
//        authService.signup(signupRequest);

        SigninRequest request = SigninRequest.builder()
                .email("test@test.com")
                .password("qwer1234")
                .build();

        // when
        Long userId = authService.signin(request);

        // then
        assertThat(userId).isNotNull();
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 틀림")
    void signinFailWithInvalidPassword() {
        // given
        User savedUser = savedUser();

        SigninRequest request = SigninRequest.builder()
                .email(savedUser.getEmail())
                .password("invalidPassword")
                .build();

        // expected
        assertThatThrownBy(() -> authService.signin(request))
                .isInstanceOf(CustomException.class)
                .hasMessage("로그인 정보가 올바르지 않습니다.")
                .extracting("errorCode").isEqualTo(ErrorCode.INVALID_SIGNIN_INFORMATION);
    }

    @Test
    @DisplayName("회원가입 성공")
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
                .isInstanceOf(CustomException.class)
                .hasMessage("이미 존재하는 이메일입니다.")
                .extracting("errorCode").isEqualTo(ErrorCode.DUPLICATED_EMAIL);

        // 여전히 하나의 사용자만 존재
        assertThat(userRepository.count()).isEqualTo(1L);
    }

    private User savedUser() {
        User savedUser = User.builder()
                .name("name")
                .email("test@test.com")
                .password(passwordEncoder.encode("qwer1234"))
                .build();
        return userRepository.save(savedUser);
    }

    private static SignupServiceRequest createSignupServiceRequest() {
        return SignupServiceRequest.builder()
                .email("test@test.com")
                .password("qwer1234")
                .name("name")
                .build();
    }
}