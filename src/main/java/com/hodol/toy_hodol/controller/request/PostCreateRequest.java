package com.hodol.toy_hodol.controller.request;

import com.hodol.toy_hodol.common.exception.CustomException;
import com.hodol.toy_hodol.common.exception.ErrorCode;
import com.hodol.toy_hodol.service.request.PostCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostCreateRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private final String title;

    @NotBlank(message = "내용은 필수입니다.")
    private final String content;

    @Builder
    public PostCreateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostCreateServiceRequest toServiceRequest() {
        return PostCreateServiceRequest.builder()
                .title(title)
                .content(content)
                .build();
    }

    public void validate() {
        // 제목으로 "admin"이 포함된 제목은 허용하지 않음
        if (this.title.contains("admin")) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "title", "제목에 'admin'을 포함할 수 없습니다.");
        }
    }
}
