package com.hodol.toy_hodol.domain.post.entity;

import com.hodol.toy_hodol.domain.post.service.request.PostCreateServiceRequest;
import com.hodol.toy_hodol.domain.post.service.request.PostEditServiceRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @Builder
    private Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static Post of(PostCreateServiceRequest request) {
        return Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }

    public void edit(PostEditServiceRequest editRequest) {
        this.title = editRequest.getTitle() != null ? editRequest.getTitle() : this.title;
        this.content = editRequest.getContent() != null ? editRequest.getContent() : this.content;
    }
}
