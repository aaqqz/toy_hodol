package com.hodol.toy_hodol.repository;

import com.hodol.toy_hodol.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
}
