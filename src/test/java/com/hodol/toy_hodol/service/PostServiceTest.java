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
    void getPageList() {
        // given
        List<Post> postList = IntStream.range(1, 31)
                .mapToObj(i -> createPost("제목" + i, "내용" + i))
                .toList();
        postRepository.saveAll(postList);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));

        // when
        Page<PostResponse> postPage = postService.getPageList(pageable);

        // then
        Assertions.assertThat(postPage.getContent()).hasSize(10)
                .extracting("title", "content")
                .containsExactlyInAnyOrder(
                        tuple("제목30", "내용30"),
                        tuple("제목29", "내용29"),
                        tuple("제목28", "내용28"),
                        tuple("제목27", "내용27"),
                        tuple("제목26", "내용26"),
                        tuple("제목25", "내용25"),
                        tuple("제목24", "내용24"),
                        tuple("제목23", "내용23"),
                        tuple("제목22", "내용22"),
                        tuple("제목21", "내용21")
                );
        Assertions.assertThat(30L).isEqualTo(postPage.getTotalElements());
    }

    public Post createPost(String title, String content) {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }
}