package com.hodol.toy_hodol.domain.post.service.request;

import com.hodol.toy_hodol.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class PostCreateServiceRequest {

    private final String title;
    private final String content;

    public Post toEntity() {
        return Post.of(this);
    }
}
