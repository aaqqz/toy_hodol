package com.hodol.toy_hodol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hodol.toy_hodol.domain.post.repository.PostRepository;
import com.hodol.toy_hodol.domain.post.controller.request.PostCreateRequest;
import com.hodol.toy_hodol.domain.post.controller.request.PostEditRequest;
import com.hodol.toy_hodol.domain.post.entity.Post;
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

//    @Test
//    @DisplayName("게시글 등록 요청시 title 은 필수")
//    void createPost_title_required() throws Exception {
//        // given
//        PostCreateRequest postCreate = PostCreateRequest.builder()
//                .content("내용")
//                .build();
//
//        // excepted
//        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(postCreate))
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("BAD_REQUEST"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("제목은 필수입니다."));
//    }
//
    @Test
    @DisplayName("게시글 등록 요청시 content 은 필수")
    void createPost_content_required() throws Exception {
        // given
        PostCreateRequest postCreate = PostCreateRequest.builder()
                .title("제목")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreate))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("내용은 필수입니다."));
        // excepted
    }

    @Test
    @DisplayName("게시글 등록 요청시 title, content 은 필수")
    void createPost_title_and_content_required() throws Exception {
        // given
        PostCreateRequest postCreate = PostCreateRequest.builder()
                .build();

        // excepted
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreate))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail.title").value("제목은 필수입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail.content").value("내용은 필수입니다."));
    }

//    @Test
//    @DisplayName("게시글의 제목으로 'admin'은 포함될 수 없다")
//    void createPost_title_invalid_admin() throws Exception {
//        // given
//        PostCreateRequest postCreate = PostCreateRequest.builder()
//                .title("admin이 되고 싶다")
//                .content("내용")
//                .build();
//
//        // excepted
//        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(postCreate))
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(ErrorCode.INVALID_REQUEST.getHttpStatus().name()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.INVALID_REQUEST.getMessage()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(ErrorCode.INVALID_REQUEST.name()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("제목에 'admin'을 포함할 수 없습니다."));
//    }
//
//    @Test
//    @DisplayName("게시글 등록")
//    void createPost() throws Exception {
//        // given
//        PostCreateRequest postCreate = PostCreateRequest.builder()
//                .title("제목")
//                .content("내용")
//                .build();
//
//        // excepted
//        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(postCreate))
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("OK"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("제목"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value("내용"));
//
//        Assertions.assertThat(1L).isEqualTo(postRepository.count());
//    }
//
//    @Test
//    @DisplayName("게시글 단건 조회")
//    void getPost() throws Exception {
//        // given
//        Post savedPost = createPost("제목", "내용");
//        postRepository.save(savedPost);
//
//        // excepted
//        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", savedPost.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("OK"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("제목"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value("내용"));
//
//        Assertions.assertThat(1L).isEqualTo(postRepository.count());
//    }
//
    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void getPost_notFound() throws Exception {
        // given
        Long postId = 1L;

        // excepted
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(ErrorCode.POST_NOT_FOUND.getHttpStatus().name()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.POST_NOT_FOUND.getMessage()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(ErrorCode.POST_NOT_FOUND.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());

        Assertions.assertThat(postRepository.count()).isEqualTo(0L);
    }
//
//    @Test
//    @DisplayName("게시글 1페이지 조회")
//    void getPageList_1page() throws Exception {
//        // given
//        List<Post> postList = IntStream.range(1, 21)
//                .mapToObj(i -> createPost("제목_" + i, "내용_" + i))
//                .toList();
//        postRepository.saveAll(postList);
//
//        // excepted
//        mockMvc.perform(MockMvcRequestBuilders.get("/posts?page=1&size=5&sort=id,desc")
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("OK"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content.length()").value(5))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].title").value("제목_20"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].content").value("내용_20"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[4].title").value("제목_16"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[4].content").value("내용_16"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalPages").value(4))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalElements").value(20))
//        ;
//
//        Assertions.assertThat(postRepository.count()).isEqualTo(20L);
//    }
//
//    @Test
//    @DisplayName("게시글 2페이지 조회")
//    void getPageList_2page() throws Exception {
//        // given
//        List<Post> postList = IntStream.range(1, 21)
//                .mapToObj(i -> createPost("제목_" + i, "내용_" + i))
//                .toList();
//        postRepository.saveAll(postList);
//
//        // excepted
//        mockMvc.perform(MockMvcRequestBuilders.get("/posts?page=2&size=5&sort=id,desc")
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("OK"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content.length()").value(5))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].title").value("제목_15"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].content").value("내용_15"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[4].title").value("제목_11"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[4].content").value("내용_11"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalPages").value(4))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalElements").value(20));
//
//        Assertions.assertThat(postRepository.count()).isEqualTo(20L);
//    }
//
//    @Test
//    @DisplayName("게시글 제목, 내용 수정")
//    void editPost_TitleAndContent() throws Exception {
//        // given
//        Post post = createPost("제목", "내용");
//        postRepository.save(post);
//
//        PostEditRequest editRequest = PostEditRequest.builder()
//                .title("제목_수정")
//                .content("내용_수정")
//                .build();
//
//        // excepted
//        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}", post.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(editRequest))
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("OK"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("제목_수정"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value("내용_수정"));
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 게시글 수정")
//    void editPost_notFound() throws Exception {
//        // given
//        Long postId = 1L;
//        PostEditRequest editRequest = PostEditRequest.builder()
//                .title("제목_수정")
//                .content("내용_수정")
//                .build();
//
//        // excepted
//        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}", postId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(editRequest))
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isNotFound())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(ErrorCode.POST_NOT_FOUND.getHttpStatus().name()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.POST_NOT_FOUND.getMessage()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(ErrorCode.POST_NOT_FOUND.name()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
//
//        Assertions.assertThat(postRepository.count()).isEqualTo(0L);
//    }
//
//    @Test
//    @DisplayName("게시글 삭제")
//    void deletePost() throws Exception {
//        // given
//        Post post = createPost("제목", "내용");
//        postRepository.save(post);
//
//        // excepted
//        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}", post.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    public Post createPost(String title, String content) {
//        return Post.builder()
//                .title(title)
//                .content(content)
//                .build();
//    }
}
