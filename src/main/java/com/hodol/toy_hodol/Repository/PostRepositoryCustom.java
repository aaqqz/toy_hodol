package com.hodol.toy_hodol.Repository;

import com.hodol.toy_hodol.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<Post> getPageList(Pageable pageable);
}
