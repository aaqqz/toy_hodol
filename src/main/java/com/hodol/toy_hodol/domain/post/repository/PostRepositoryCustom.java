package com.hodol.toy_hodol.domain.post.repository;

import com.hodol.toy_hodol.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<Post> getPageList(Pageable pageable);
}
