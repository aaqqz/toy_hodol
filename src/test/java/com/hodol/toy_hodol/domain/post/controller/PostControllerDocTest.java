package com.hodol.toy_hodol.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hodol.toy_hodol.domain.post.controller.request.PostCreateRequest;
import com.hodol.toy_hodol.domain.post.entity.Post;
import com.hodol.toy_hodol.domain.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.hodol.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("게시글 단건 조회")
    void getPost() throws Exception {
        // given
        Post savedPost = createPost("제목", "내용");
        postRepository.save(savedPost);

        // excepted
        mockMvc.perform(get("/posts/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(document("post-inquiry",
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("http status code"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("errorCode").description("응답 에러 코드"),
                                fieldWithPath("data.id").description("게시글 ID"),
                                fieldWithPath("data.title").description("제목"),
                                fieldWithPath("data.content").description("내용")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 등록")
    void createPost() throws Exception {
        // given
        PostCreateRequest postCreate = PostCreateRequest.builder()
                .title("제목")
                .content("내용")
                .build();

        // excepted
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreate))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(document("post-create",
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용")
//                                fieldWithPath("content").description("내용").optional()
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("http status code"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("errorCode").description("응답 에러 코드"),
                                fieldWithPath("data.id").description("게시글 ID"),
                                fieldWithPath("data.title").description("제목"),
                                fieldWithPath("data.content").description("내용")
                        )
                ));
    }

    public Post createPost(String title, String content) {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }
}


