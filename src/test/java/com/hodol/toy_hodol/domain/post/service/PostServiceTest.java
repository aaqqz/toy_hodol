package com.hodol.toy_hodol.domain.post.service;

import com.hodol.toy_hodol.common.exception.PostNotFoundException;
import com.hodol.toy_hodol.domain.post.entity.Post;
import com.hodol.toy_hodol.domain.post.repository.PostRepository;
import com.hodol.toy_hodol.domain.post.service.request.PostCreateServiceRequest;
import com.hodol.toy_hodol.domain.post.service.request.PostEditServiceRequest;
import com.hodol.toy_hodol.domain.post.service.response.PostResponse;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor
class PostServiceTest {

    final PostService postService;
    final PostRepository postRepository;
    final EntityManager entityManager;
//    @BeforeEach
//    void clean() {
//        postRepository.deleteAllInBatch();
//    }

    @Test
    @DisplayName("게시글 등록")
    void create() {
        // given
        PostCreateServiceRequest request = PostCreateServiceRequest.builder()
                .title("제목")
                .content("내용")
                .build();

        // when
        PostResponse postResponse = postService.create(request);

        // then
        assertThat(postResponse.getId()).isNotNull();
        assertThat(postRepository.count()).isEqualTo(1L);
        assertThat(postResponse.getTitle()).isEqualTo(request.getTitle());
        assertThat(postResponse.getContent()).isEqualTo(request.getContent());
    }

    @Test
    @DisplayName("게시글 단건 조회")
    void get() {
        // given
        Post post = createPost();

        // when
        PostResponse postResponse = postService.get(post.getId());

        // then
        assertThat(postResponse.getId()).isNotNull();
        assertThat(postRepository.count()).isEqualTo(1L);
        assertThat(postResponse.getTitle()).isEqualTo(post.getTitle());
        assertThat(postResponse.getContent()).isEqualTo(post.getContent());
    }

    @Test
    @DisplayName("게시글 단건 조회_실패")
    void getFail() {
        // given
        Post post = createPost();

        // excepted
        assertThatThrownBy(() -> postService.get(post.getId() + 1L))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("게시글 1페이지 조회")
    void getPageList_1page() {
        // given
        List<Post> postList = IntStream.range(1, 21)
                .mapToObj(i -> createPost("제목" + i, "내용" + i))
                .toList();
        postRepository.saveAll(postList);

        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");

        // when
        Page<PostResponse> postPage = postService.getPageList(pageable);

        // then
        assertThat(postPage.getContent()).hasSize(5)
                .extracting("title", "content")
                .containsExactlyInAnyOrder(
                        tuple("제목20", "내용20"),
                        tuple("제목19", "내용19"),
                        tuple("제목18", "내용18"),
                        tuple("제목17", "내용17"),
                        tuple("제목16", "내용16")
                );
        assertThat(postPage.getTotalElements()).isEqualTo(20L);
    }

    @Test
    @DisplayName("게시글 2페이지 조회")
    void getPageList_2page() {
        // given
        List<Post> postList = IntStream.range(1, 21)
                .mapToObj(i -> createPost("제목" + i, "내용" + i))
                .toList();
        postRepository.saveAll(postList);

        Pageable pageable = PageRequest.of(1, 5, Sort.Direction.DESC, "id");

        // when
        Page<PostResponse> postPage = postService.getPageList(pageable);

        // then
        assertThat(postPage.getContent()).hasSize(5)
                .extracting("title", "content")
                .containsExactlyInAnyOrder(
                        tuple("제목15", "내용15"),
                        tuple("제목14", "내용14"),
                        tuple("제목13", "내용13"),
                        tuple("제목12", "내용12"),
                        tuple("제목11", "내용11")
                );
        assertThat(postPage.getTotalElements()).isEqualTo(20L);
    }

    @Test
    @DisplayName("게시글 제목 수정")
    void editPostTitle() {
        // given
        Post post = createPost();

        PostEditServiceRequest request = PostEditServiceRequest.builder()
                .title("제목_변경")
                .build();

        // when
        postService.edit(post.getId(), request);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        assertThat(changedPost.getTitle()).isEqualTo(request.getTitle());
        assertThat(changedPost.getContent()).isEqualTo(post.getContent());
    }

    @Test
    @DisplayName("게시글 내용 수정")
    void editPostContent() {
        // given
        Post post = createPost();

        PostEditServiceRequest request = PostEditServiceRequest.builder()
                .content("내용_변경")
                .build();

        // when
        postService.edit(post.getId(), request);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        assertThat(changedPost.getTitle()).isEqualTo(post.getTitle());
        assertThat(changedPost.getContent()).isEqualTo(request.getContent());
    }

    @Test
    @DisplayName("게시글 제목, 내용 수정")
    void editPostTitleAndContent() {
        // given
        Post post = createPost();

        PostEditServiceRequest request = PostEditServiceRequest.builder()
                .title("제목_변경")
                .content("내용_변경")
                .build();

        // when
        postService.edit(post.getId(), request);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        assertThat(changedPost.getTitle()).isEqualTo(request.getTitle());
        assertThat(changedPost.getContent()).isEqualTo(request.getContent());
    }

    @Test
    @DisplayName("게시글 수정_실패")
    void editFail() {
        // given
        Post post = createPost();

        PostEditServiceRequest request = PostEditServiceRequest.builder()
                .title("제목_변경")
                .content("내용_변경")
                .build();

        // when
        postService.edit(post.getId(), request);

        // exception
        assertThatThrownBy(() -> postService.edit(post.getId() + 1L, request))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("게시글 삭제")
    void delete() {
        // given
        Post post = createPost();

        // when
        postService.delete(post.getId());

        // then
        assertThat(postRepository.findAll()).isEmpty();
        assertThat(postRepository.count()).isEqualTo(0L);
    }

    @Test
    @DisplayName("게시글 삭제_실패")
    void deleteFail() {
        // given
        Post post = createPost();

        // exception
        assertThatThrownBy(() -> postService.delete(post.getId() + 1L))
                .isInstanceOf(PostNotFoundException.class);
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