package com.hodol.toy_hodol.service;

import com.hodol.toy_hodol.repository.PostRepository;
import com.hodol.toy_hodol.common.exception.CustomException;
import com.hodol.toy_hodol.common.exception.ErrorCode;
import com.hodol.toy_hodol.domain.Post;
import com.hodol.toy_hodol.service.request.PostCreateServiceRequest;
import com.hodol.toy_hodol.service.request.PostEditServiceRequest;
import com.hodol.toy_hodol.service.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostResponse create(PostCreateServiceRequest request) {
        Post post = postRepository.save(request.toEntity());
        return PostResponse.of(post);
    }

    @Transactional(readOnly = true)
    public PostResponse get(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        return PostResponse.of(post);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getPageList(Pageable pageable) {
        Page<Post> postPage = postRepository.getPageList(pageable);
        return postPage.map(PostResponse::of);
    }

    @Transactional
    public PostResponse edit(Long postId, PostEditServiceRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        post.edit(request);
        return PostResponse.of(post);
    }

    public void delete(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        postRepository.delete(post);
    }
}
