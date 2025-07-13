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

import java.util.List;

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
    @DisplayName("글 다건 조회")
    void getList() {
        // given
        Post post1 = createPost("제목1", "내용1");
        Post post2 = createPost("제목2", "내용2");
        Post post3 = createPost("제목3", "내용3");
        Post post4 = createPost("제목4", "내용4");
        List<Post> postList = List.of(post1, post2, post3, post4);
        postRepository.saveAll(postList);

        // when
        List<PostResponse> postResponseList = postService.getList();

        // then
        Assertions.assertThat(postResponseList).hasSize(4)
                .extracting("title", "content")
                .containsExactlyInAnyOrder(
                        tuple("제목1", "내용1"),
                        tuple("제목2", "내용2"),
                        tuple("제목3", "내용3"),
                        tuple("제목4", "내용4")
                );
    }

    public Post createPost(String title, String content) {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }
}