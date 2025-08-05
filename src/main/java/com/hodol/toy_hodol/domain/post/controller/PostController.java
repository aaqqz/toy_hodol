package com.hodol.toy_hodol.domain.post.controller;

import com.hodol.toy_hodol.common.resolver.data.UserSession;
import com.hodol.toy_hodol.common.response.ApiResponse;
import com.hodol.toy_hodol.domain.post.controller.request.PostCreateRequest;
import com.hodol.toy_hodol.domain.post.controller.request.PostEditRequest;
import com.hodol.toy_hodol.domain.post.service.PostService;
import com.hodol.toy_hodol.domain.post.service.response.PostResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/foo")
    public Long foo(UserSession userSession) {
        log.info("##########1 {}", userSession.getId());
        log.info("##########2 {}", userSession.id);

        return userSession.getId();
    }

    @GetMapping("/bar")
    public String bar() {
        return "인증이 필요 없는 엔드포인트입니다.";
    }

    @PostMapping
    public ApiResponse<PostResponse> create(@RequestBody @Valid PostCreateRequest request) {
        request.validate(); // 유효성 검사

        return ApiResponse.success(postService.create(request.toServiceRequest()));
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> get(@PathVariable Long postId) {
        return ApiResponse.success(postService.get(postId));
    }

    @GetMapping
    public ApiResponse<Page<PostResponse>> getPageList(Pageable pageable) {
        return ApiResponse.success(postService.getPageList(pageable));
    }

    @PatchMapping("/{postId}")
    public ApiResponse<PostResponse> edit(@PathVariable Long postId, @RequestBody @Valid PostEditRequest request) {
        return ApiResponse.success(postService.edit(postId, request.toServiceRequest()));
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<Void> delete(@PathVariable Long postId) {
        postService.delete(postId);
        return ApiResponse.success();
    }
}
