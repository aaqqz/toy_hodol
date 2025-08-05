package com.hodol.toy_hodol.domain.post.repository;

import com.hodol.toy_hodol.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
}
