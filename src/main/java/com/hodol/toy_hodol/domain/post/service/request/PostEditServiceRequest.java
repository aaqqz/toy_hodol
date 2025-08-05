package com.hodol.toy_hodol.domain.post.service.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class PostEditServiceRequest {

    private final String title;
    private final String content;
}
