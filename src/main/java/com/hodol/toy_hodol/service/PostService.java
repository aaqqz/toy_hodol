package com.hodol.toy_hodol.service;

import com.hodol.toy_hodol.Repository.PostRepository;
import com.hodol.toy_hodol.domain.Post;
import com.hodol.toy_hodol.service.request.PostCreateServiceRequest;
import com.hodol.toy_hodol.service.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostResponse create(PostCreateServiceRequest request) {
        Post post = postRepository.save(request.toEntity());
        return PostResponse.of(post);
    }

    public PostResponse get(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));
        return PostResponse.of(post);
    }

    public Page<PostResponse> getPageList(Pageable pageable) {
        Page<Post> postPage = postRepository.getPageList(pageable);
        return postPage.map(PostResponse::of);
    }
}
