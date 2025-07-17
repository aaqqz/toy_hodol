package com.hodol.toy_hodol.service;

import com.hodol.toy_hodol.Repository.PostRepository;
import com.hodol.toy_hodol.domain.Post;
import com.hodol.toy_hodol.service.request.PostCreateServiceRequest;
import com.hodol.toy_hodol.service.response.PostResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("글 작성")
    void create() {
        // given
        PostCreateServiceRequest request = PostCreateServiceRequest.builder()
                .title("제목")
                .content("내용")
                .build();

        // when
        PostResponse postResponse = postService.create(request);

        // then
        Assertions.assertThat(postResponse.getId()).isNotNull();
        Assertions.assertThat(1L).isEqualTo(postRepository.count());
        Assertions.assertThat("제목").isEqualTo(postResponse.getTitle());
        Assertions.assertThat("내용").isEqualTo(postResponse.getContent());
    }

    @Test
    @DisplayName("글 단건 조회")
    void get() {
        // given
        Post post = createPost("제목", "내용");
        postRepository.save(post);

        // when
        PostResponse postResponse = postService.get(post.getId());

        // then
        Assertions.assertThat(postResponse.getId()).isNotNull();
        Assertions.assertThat(1L).isEqualTo(postRepository.count());
        Assertions.assertThat("제목").isEqualTo(postResponse.getTitle());
        Assertions.assertThat("내용").isEqualTo(postResponse.getContent());
    }

    @Test
    @DisplayName("글 1페이지 조회")
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
        Assertions.assertThat(postPage.getContent()).hasSize(5)
                .extracting("title", "content")
                .containsExactlyInAnyOrder(
                        tuple("제목20", "내용20"),
                        tuple("제목19", "내용19"),
                        tuple("제목18", "내용18"),
                        tuple("제목17", "내용17"),
                        tuple("제목16", "내용16")
                );
        Assertions.assertThat(20L).isEqualTo(postPage.getTotalElements());
    }

    @Test
    @DisplayName("글 2페이지 조회")
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
        Assertions.assertThat(postPage.getContent()).hasSize(5)
                .extracting("title", "content")
                .containsExactlyInAnyOrder(
                        tuple("제목15", "내용15"),
                        tuple("제목14", "내용14"),
                        tuple("제목13", "내용13"),
                        tuple("제목12", "내용12"),
                        tuple("제목11", "내용11")
                );
        Assertions.assertThat(20L).isEqualTo(postPage.getTotalElements());
    }

    public Post createPost(String title, String content) {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }
}