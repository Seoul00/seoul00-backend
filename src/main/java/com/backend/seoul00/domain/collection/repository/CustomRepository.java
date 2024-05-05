package com.backend.seoul00.domain.collection.repository;

import com.backend.seoul00.domain.collection.entity.CollectionJpaEntity;
import com.backend.seoul00.domain.collection.entity.Type;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.backend.seoul00.domain.collection.entity.QCollectionJpaEntity.collectionJpaEntity;

@Repository
public class CustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    // TODO : 사용자 위치 기반 데이터 API

    public Slice<CollectionJpaEntity> searchPosByQuery(String query, List<Type> sort, Pageable pageable) {
        int pageSize = pageable.getPageSize();

        List<CollectionJpaEntity> qp = jpaQueryFactory.selectFrom(collectionJpaEntity)
                .where(collectionJpaEntity.type.in(sort),
                        collectionJpaEntity.address.contains(query)
                                .and(collectionJpaEntity.longitude.isNotNull())
                                .and(collectionJpaEntity.latitude.isNotNull()))
                .offset(pageable.getOffset())
                .limit(pageSize + 1)
                .fetch();


        boolean hasNext = false;
        if (qp.size() > pageSize) {
            qp.remove(pageSize);
            hasNext = true;
        }

        return new SliceImpl<>(qp, pageable, hasNext);
    }
}
