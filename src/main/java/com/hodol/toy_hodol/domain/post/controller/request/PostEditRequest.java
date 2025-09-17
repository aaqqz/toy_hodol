package com.hodol.toy_hodol.domain.post.controller.request;

import com.hodol.toy_hodol.domain.post.service.request.PostEditServiceRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEditRequest {

    private final String title;

    private final String content;

    @Builder
    public PostEditRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostEditServiceRequest toServiceRequest() {
        return PostEditServiceRequest.builder()
                .title(title)
                .content(content)
                .build();
    }
}
