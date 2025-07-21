package com.hodol.toy_hodol.controller;

import com.hodol.toy_hodol.common.response.ApiResponse;
import com.hodol.toy_hodol.controller.request.PostCreateRequest;
import com.hodol.toy_hodol.controller.request.PostEditRequest;
import com.hodol.toy_hodol.service.PostService;
import com.hodol.toy_hodol.service.response.PostResponse;
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

    @PostMapping
    public ApiResponse<PostResponse> create(@RequestBody @Valid PostCreateRequest request) {
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
}
