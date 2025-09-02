package com.hodol.toy_hodol.domain.auth.service;

import com.hodol.toy_hodol.common.exception.CustomException;
import com.hodol.toy_hodol.common.exception.ErrorCode;
import com.hodol.toy_hodol.domain.auth.controller.request.SigninRequest;
import com.hodol.toy_hodol.domain.auth.entity.User;
import com.hodol.toy_hodol.domain.auth.repository.UserRepository;
import com.hodol.toy_hodol.domain.auth.service.request.SignupServiceRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

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
        User savedUser = User.builder()
                .name("name")
                .email("test@test.com")
                .password(passwordEncoder.encode("1234"))
                .build();
        userRepository.save(savedUser);

//        SignupServiceRequest signupRequest = createSignupServiceRequest();
//        authService.signup(signupRequest);

        SigninRequest request = SigninRequest.builder()
                .email("test@test.com")
                .password("1234")
                .build();

        // when
        Long userId = authService.signin(request);

        // then
        Assertions.assertThat(userId).isNotNull();
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 틀림")
    void signinFailWithInvalidPassword() {
        // given
        SignupServiceRequest signupRequest = createSignupServiceRequest();
        authService.signup(signupRequest);

        SigninRequest request = SigninRequest.builder()
                .email(signupRequest.getEmail())
                .password("invalidPassword")
                .build();

        // expected
        Assertions.assertThatThrownBy(() -> authService.signin(request))
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
        Assertions.assertThat(userRepository.count()).isEqualTo(1L);

        User user = userRepository.findAll().iterator().next();
        Assertions.assertThat(user.getName()).isEqualTo(request.getName());
        Assertions.assertThat(user.getEmail()).isEqualTo(request.getEmail());
        Assertions.assertThat(user.getPassword()).isNotEqualTo(request.getPassword());
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void signupFailWithDuplicatedEmail() {
        // given
        SignupServiceRequest firstRequest = createSignupServiceRequest();
        authService.signup(firstRequest); // 첫 번째 회원가입

        SignupServiceRequest duplicatedRequest = SignupServiceRequest.builder()
                .name("diffentName")
                .email("test@test.com") // 동일한 이메일
                .password("differentPassword")
                .build();

        // expected
        Assertions.assertThatThrownBy(() -> authService.signup(duplicatedRequest))
                .isInstanceOf(CustomException.class)
                .hasMessage("이미 존재하는 이메일입니다.")
                .extracting("errorCode").isEqualTo(ErrorCode.DUPLICATED_EMAIL);;

        // 여전히 하나의 사용자만 존재
        Assertions.assertThat(userRepository.count()).isEqualTo(1L);
    }


    private static SignupServiceRequest createSignupServiceRequest() {
        return SignupServiceRequest.builder()
                .name("name")
                .email("test@test.com")
                .password("qwer1234")
                .build();
    }
}