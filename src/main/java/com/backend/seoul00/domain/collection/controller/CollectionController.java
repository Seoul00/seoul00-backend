package com.backend.seoul00.domain.collection.controller;

import com.backend.seoul00.domain.collection.service.CollectionService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CollectionController {

    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    // TODO : 사용자 위치 기반 데이터 API

    // TODO : 검색 데이터 API
}
