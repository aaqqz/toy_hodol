package com.hodol.toy_hodol.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hodol.toy_hodol.domain.auth.controller.request.AuthLoginRequest;
import com.hodol.toy_hodol.domain.auth.entity.Session;
import com.hodol.toy_hodol.domain.auth.entity.User;
import com.hodol.toy_hodol.domain.auth.repository.SessionRepository;
import com.hodol.toy_hodol.domain.auth.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SessionRepository sessionRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("로그인 성공")
    void login() throws Exception {
        //given
        createUser();
        AuthLoginRequest loginRequest = AuthLoginRequest.builder()
                .email("test@gmail.com")
                .password("1234")
                .build();

        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
        ;
    }

    @Test
    @Transactional // todo 좋은 방법은 아닌거 같다
    @DisplayName("로그인 성공 후 session 응답")
    void getSessionAfterLogin() throws Exception {
        //given
        User user = createUser();
        AuthLoginRequest loginRequest = AuthLoginRequest.builder()
                .email("test@gmail.com")
                .password("1234")
                .build();

        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.accessToken", Matchers.notNullValue()))
        ;

        Assertions.assertThat(user.getSessions().size( )).isEqualTo(1);
    }

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지에 접속한다 /foo")
    void auth_page_after_login() throws Exception {
        //given
        User user = User.builder()
                .name("test")
                .email("test@gmail.com")
                .password("1234")
                .build();
        Session session = user.addSession();
        userRepository.save(user);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/foo")
                        .header("Authorization", session.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
        ;
    }

    @Test
    @DisplayName("로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속할 수 없다")
    void auth_page_after_login_fail() throws Exception {
        //given
        User user = User.builder()
                .name("test")
                .email("test@gmail.com")
                .password("1234")
                .build();
        Session session = user.addSession();
        userRepository.save(user);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/foo")
                        .header("Authorization", session.getAccessToken() + "fail")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    private User createUser() {
        User user = User.builder()
                .name("test")
                .email("test@gmail.com")
                .password("1234")
                .build();

        return userRepository.save(user);
    }
}