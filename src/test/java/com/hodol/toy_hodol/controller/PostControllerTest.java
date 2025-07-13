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

import java.util.List;

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
        postRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("등록 요청시 title 은 필수")
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
    @DisplayName("등록 요청시 content 은 필수")
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
    @DisplayName("등록 요청시 DB에 값이 저장")
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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("제목"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value("내용"));

        Assertions.assertThat(1L).isEqualTo(postRepository.count());
    }

    @Test
    @DisplayName("단건 조회")
    void post_get() throws Exception {
        // given
        Post savedPost = createPost("제목", "내용");
        postRepository.save(savedPost);

        // expectation
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", savedPost.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("제목"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value("내용"));

        Assertions.assertThat(1L).isEqualTo(postRepository.count());
    }

    @Test
    @DisplayName("다건 조회")
    void post_getList() throws Exception {
        // given
        Post post1 = createPost("제목_1", "내용_1");
        Post post2 = createPost("제목_2", "내용_2");
        Post post3 = createPost("제목_3", "내용_3");
        Post post4 = createPost("제목_4", "내용_4");
        List<Post> postList = List.of(post1, post2, post3, post4);
        postRepository.saveAll(postList);


        // expectation
        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].title").value("제목_1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].content").value("내용_1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].title").value("제목_2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].content").value("내용_2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[3].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[3].title").value("제목_4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[3].content").value("내용_4"))
        ;

        Assertions.assertThat(4L).isEqualTo(postRepository.count());
    }

    public Post createPost(String title, String content) {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }
}
