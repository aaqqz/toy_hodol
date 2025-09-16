package com.hodol.toy_hodol.domain.post.service;

import com.hodol.toy_hodol.common.exception.PostNotFoundException;
import com.hodol.toy_hodol.domain.post.repository.PostRepository;
import com.hodol.toy_hodol.domain.post.entity.Post;
import com.hodol.toy_hodol.domain.post.service.request.PostCreateServiceRequest;
import com.hodol.toy_hodol.domain.post.service.request.PostEditServiceRequest;
import com.hodol.toy_hodol.domain.post.service.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostResponse create(PostCreateServiceRequest request) {
        Post post = postRepository.save(request.toEntity());

        return PostResponse.of(post);
    }

    @Transactional(readOnly = true)
    public PostResponse get(Long postId) {
        Post post = findById(postId);

        return PostResponse.of(post);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getPageList(Pageable pageable) {
        Page<Post> postPage = postRepository.getPageList(pageable);

        return postPage.map(PostResponse::of);
    }

    public PostResponse edit(Long postId, PostEditServiceRequest request) {
        Post post = findById(postId);
        post.edit(request);

        return PostResponse.of(post);
    }

    public void delete(Long postId) {
        Post post = findById(postId);

        postRepository.delete(post);
    }

    private Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("존재하지 않는 글입니다."));
    }
}
