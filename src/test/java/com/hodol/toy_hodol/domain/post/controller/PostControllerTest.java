package com.hodol.toy_hodol.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hodol.toy_hodol.domain.post.controller.request.PostCreateRequest;
import com.hodol.toy_hodol.domain.post.controller.request.PostEditRequest;
import com.hodol.toy_hodol.domain.post.entity.Post;
import com.hodol.toy_hodol.domain.post.repository.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import java.util.List;
import java.util.stream.IntStream;

import static com.hodol.toy_hodol.AssertThatUtils.equalsTo;
import static com.hodol.toy_hodol.AssertThatUtils.notEmpty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class PostControllerTest {

    final MockMvcTester mockMvcTester;
    final ObjectMapper objectMapper;
    final PostRepository postRepository;
    final EntityManager entityManager;

    @Test
    @DisplayName("게시글 등록")
    void create() throws Exception {
        // given
        PostCreateRequest postCreate = PostCreateRequest.builder()
                .title("제목")
                .content("내용")
                .build();
        String requestJson = objectMapper.writeValueAsString(postCreate);

        // excepted
        MvcTestResult result = mockMvcTester.post()
                .uri("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .exchange();

        assertThat(result)
                .apply(print())
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.statusCode", equalsTo("OK"))
                .hasPathSatisfying("$.data.id", notEmpty())
                .hasPathSatisfying("$.data.title", equalsTo(postCreate.getTitle()))
                .hasPathSatisfying("$.data.content", equalsTo(postCreate.getContent()));
    }

    @Test
    @DisplayName("게시글 등록_실패 - title 필수")
    void createFailRequiredTitle() throws Exception {
        // given
        PostCreateRequest postCreate = PostCreateRequest.builder()
                .content("내용")
                .build();
        String requestJson = objectMapper.writeValueAsString(postCreate);

        // excepted
        MvcTestResult result = mockMvcTester.post()
                .uri("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .exchange();

        assertThat(result)
                .apply(print())
                .hasStatus(HttpStatus.BAD_REQUEST)
                .bodyJson()
                .hasPathSatisfying("$.title", equalsTo("Bad Request"));
    }

    @Test
    @DisplayName("게시글 등록_실패 - content 필수")
    void createFailRequiredContent() throws Exception {
        // given
        PostCreateRequest postCreate = PostCreateRequest.builder()
                .title("제목")
                .build();
        String requestJson = objectMapper.writeValueAsString(postCreate);

        // excepted
        MvcTestResult result = mockMvcTester.post()
                .uri("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .exchange();

        assertThat(result)
                .apply(print())
                .hasStatus(HttpStatus.BAD_REQUEST)
                .bodyJson()
                .hasPathSatisfying("$.title", equalsTo("Bad Request"));
    }

    @Test
    @DisplayName("게시글 등록_실패 - title, content 필수")
    void createFailRequiredTitleAndContent() throws Exception {
        // given
        PostCreateRequest postCreate = PostCreateRequest.builder()
                .build();
        String requestJson = objectMapper.writeValueAsString(postCreate);

        // excepted
        MvcTestResult result = mockMvcTester.post()
                .uri("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .exchange();

        assertThat(result)
                .apply(print())
                .hasStatus(HttpStatus.BAD_REQUEST)
                .bodyJson()
                .hasPathSatisfying("$.title", equalsTo("Bad Request"))
                .hasPathSatisfying("$.detail.title", equalsTo("제목은 필수입니다."))
                .hasPathSatisfying("$.detail.content", equalsTo("내용은 필수입니다."));
    }

    @Test
    @DisplayName("게시글 단건 조회")
    void get() {
        // given
        Post savedPost = createPost();

        // excepted
        MvcTestResult result = mockMvcTester.get()
                .uri("/posts/{postId}", savedPost.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(result)
                .apply(print())
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.statusCode", equalsTo("OK"))
                .hasPathSatisfying("$.data.id", notEmpty())
                .hasPathSatisfying("$.data.title", equalsTo(savedPost.getTitle()))
                .hasPathSatisfying("$.data.content", equalsTo(savedPost.getContent()));

        assertThat(postRepository.count()).isEqualTo(1L);
    }

    @Test
    @DisplayName("게시글 단건 조회_실패")
    void getFailNotFound() {
        // given
        Long postId = 1L;

        // excepted
        MvcTestResult result = mockMvcTester.get()
                .uri("/posts/{postId}", postId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(result)
                .apply(print())
                .hasStatus(HttpStatus.NOT_FOUND)
                .bodyJson()
                .hasPathSatisfying("$.detail", equalsTo("존재하지 않는 글입니다."));

        assertThat(postRepository.count()).isEqualTo(0L);
    }

    @Test
    @DisplayName("게시글 1페이지 조회")
    void getPageList_1page() {
        // given
        List<Post> postList = IntStream.range(1, 11)
                .mapToObj(i -> createPost("제목_" + i, "내용_" + i))
                .toList();
        postRepository.saveAll(postList);

        // excepted
        MvcTestResult result = mockMvcTester.get()
                .uri("/posts?page=1&size=5&sort=id,desc")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(result)
                .apply(print())
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.statusCode", equalsTo("OK"))
                .hasPathSatisfying("$.data.content.length()", equalsTo(5))
                .hasPathSatisfying("$.data.content[0].id", notEmpty())
                .hasPathSatisfying("$.data.content[0].title", equalsTo("제목_10"))
                .hasPathSatisfying("$.data.content[0].content", equalsTo("내용_10"))
                .hasPathSatisfying("$.data.content[4].title", equalsTo("제목_6"))
                .hasPathSatisfying("$.data.content[4].content", equalsTo("내용_6"))
                .hasPathSatisfying("$.data.totalPages", equalsTo(2))
                .hasPathSatisfying("$.data.totalElements", equalsTo(10));

        assertThat(postRepository.count()).isEqualTo(10L);
    }


    @Test
    @DisplayName("게시글 2페이지 조회")
    void getPageList_2page() throws Exception {
        // given
        List<Post> postList = IntStream.range(1, 11)
                .mapToObj(i -> createPost("제목_" + i, "내용_" + i))
                .toList();
        postRepository.saveAll(postList);

        // excepted
        MvcTestResult result = mockMvcTester.get()
                .uri("/posts?page=2&size=5&sort=id,desc")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(result)
                .apply(print())
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.statusCode", equalsTo("OK"))
                .hasPathSatisfying("$.data.content.length()", equalsTo(5))
                .hasPathSatisfying("$.data.content[0].id", notEmpty())
                .hasPathSatisfying("$.data.content[0].title", equalsTo("제목_5"))
                .hasPathSatisfying("$.data.content[0].content", equalsTo("내용_5"))
                .hasPathSatisfying("$.data.content[4].title", equalsTo("제목_1"))
                .hasPathSatisfying("$.data.content[4].content", equalsTo("내용_1"))
                .hasPathSatisfying("$.data.totalPages", equalsTo(2))
                .hasPathSatisfying("$.data.totalElements", equalsTo(10));

        assertThat(postRepository.count()).isEqualTo(10L);
    }

    @Test
    @DisplayName("게시글 수정")
    void edit() throws Exception {
        // given
        Post savedPost = createPost();

        PostEditRequest postEdit = PostEditRequest.builder()
                .title("제목_수정")
                .content("내용_수정")
                .build();
        String requestJson = objectMapper.writeValueAsString(postEdit);

        // excepted
        MvcTestResult result = mockMvcTester.patch()
                .uri("/posts/{postId}", savedPost.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .exchange();

        assertThat(result)
                .apply(print())
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.statusCode", equalsTo("OK"))
                .hasPathSatisfying("$.data.id", notEmpty())
                .hasPathSatisfying("$.data.title", equalsTo(postEdit.getTitle()))
                .hasPathSatisfying("$.data.content", equalsTo(postEdit.getContent()));
    }

    @Test
    @DisplayName("게시글 삭제")
    void delete() throws Exception {
        // given
        Post post = createPost();

        // excepted
        MvcTestResult result = mockMvcTester.delete()
                .uri("/posts/{postId}", post.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(result)
                .apply(print())
                .hasStatusOk();
    }

    private Post createPost() {
        return createPost("제목", "내용");
    }

    private Post createPost(String title, String content) {
        Post post = Post.builder()
                .title(title)
                .content(content)
                .build();
        postRepository.save(post);

        entityManager.flush();
        entityManager.clear();

        return post;
    }
}
