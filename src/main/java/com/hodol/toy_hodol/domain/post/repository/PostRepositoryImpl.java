package com.hodol.toy_hodol.domain.post.repository;

import com.hodol.toy_hodol.domain.post.entity.Post;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.hodol.toy_hodol.domain.post.entity.QPost.post;


@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> getPageList(Pageable pageable) {
        List<Post> content = queryFactory
                .selectFrom(post)
                .orderBy(post.id.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        JPAQuery<Long> count = queryFactory
                .select(post.count())
                .from(post);

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }
}
