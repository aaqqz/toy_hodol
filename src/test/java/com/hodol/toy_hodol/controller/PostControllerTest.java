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
import java.util.stream.IntStream;

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
    @DisplayName("페이지 조회_page1")
    void post_getPageList_page1() throws Exception {
        // given
        List<Post> postList = IntStream.range(1, 21)
                .mapToObj(i -> createPost("제목_" + i, "내용_" + i))
                .toList();
        postRepository.saveAll(postList);

        // expectation
        mockMvc.perform(MockMvcRequestBuilders.get("/posts?page=1&size=5&sort=id,desc")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content.length()").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].title").value("제목_20"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].content").value("내용_20"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[4].title").value("제목_16"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[4].content").value("내용_16"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalPages").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalElements").value(20))
        ;

        Assertions.assertThat(20L).isEqualTo(postRepository.count());
    }

    @Test
    @DisplayName("페이지 조회_page2")
    void post_getPageList_page2() throws Exception {
        // given
        List<Post> postList = IntStream.range(1, 21)
                .mapToObj(i -> createPost("제목_" + i, "내용_" + i))
                .toList();
        postRepository.saveAll(postList);

        // expectation
        mockMvc.perform(MockMvcRequestBuilders.get("/posts?page=2&size=5&sort=id,desc")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content.length()").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].title").value("제목_15"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].content").value("내용_15"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[4].title").value("제목_11"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[4].content").value("내용_11"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalPages").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalElements").value(20))
        ;

        Assertions.assertThat(20L).isEqualTo(postRepository.count());
    }

    public Post createPost(String title, String content) {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }
}
