package com.backend.seoul00.domain.collection.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

@Repository
public class CustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    // TODO : 사용자 위치 기반 데이터 API

    // TODO : 검색 데이터 API
}
