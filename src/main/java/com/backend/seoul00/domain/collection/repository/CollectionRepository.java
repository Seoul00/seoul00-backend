package com.backend.seoul00.domain.collection.repository;

import com.backend.seoul00.domain.collection.entity.CollectionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<CollectionJpaEntity, Long> {
}
