package com.hodol.toy_hodol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hodol.toy_hodol.Repository.PostRepository;
import com.hodol.toy_hodol.controller.request.PostCreateRequest;
import com.hodol.toy_hodol.domain.Post;
import org.assertj.core.api.Assertions;
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

@AutoConfigureMockMvc
@SpringBootTest
//@WebMvcTest(controllers = PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

//    @MockitoBean
//    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청시 title 은 필수다")
    void post_title_required() throws Exception {
        // given
        PostCreateRequest postCreate = PostCreateRequest.builder()
                .content("내용")
                .build();

        // expectation
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreate))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("제목은 필수입니다."));
    }

    @Test
    @DisplayName("/posts 요청시 content 은 필수다")
    void post_content_required() throws Exception {
        // given
        PostCreateRequest postCreate = PostCreateRequest.builder()
                .title("제목")
                .build();

        // expectation
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreate))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value("내용은 필수입니다."));
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다")
    void post_create() throws Exception {
        // given
        PostCreateRequest postCreate = PostCreateRequest.builder()
                .title("제목")
                .content("내용")
                .build();

        // expectation
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreate))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
//                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("BAD_REQUEST"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("제목은 필수입니다."));

        // then
        Assertions.assertThat(1L).isEqualTo(postRepository.count());

        Post post = postRepository.findAll().get(0);
        Assertions.assertThat(post.getTitle()).isEqualTo(postCreate.getTitle());
        Assertions.assertThat(post.getContent()).isEqualTo(postCreate.getContent());
    }
}