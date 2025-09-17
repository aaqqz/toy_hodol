package com.hodol.toy_hodol.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hodol.toy_hodol.domain.auth.controller.request.SignupRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import static com.hodol.toy_hodol.AssertThatUtils.equalsTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class AuthControllerTest {

    final MockMvcTester mockMvcTester;
    final ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입")
    void signup() throws Exception {
        //given
        SignupRequest request = SignupRequest.builder()
                .email("test@gmail.com")
                .password("1234")
                .name("name")
                .build();
        String requestJson = objectMapper.writeValueAsString(request);

        //expected
        MvcTestResult result = mockMvcTester.post()
                .uri("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .exchange();

        assertThat(result)
                .apply(print())
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.statusCode", equalsTo("OK"));
    }
}
