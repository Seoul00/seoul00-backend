package com.backend.seoul00.domain.collection.controller;

import com.backend.seoul00.domain.collection.dto.ResponseCounter;
import com.backend.seoul00.domain.collection.dto.SearchResponseDto;
import com.backend.seoul00.domain.collection.service.CollectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CollectionController {

    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    // 사용자 위치 기반 데이터 API
    @GetMapping("/near")
    public ResponseEntity<ResponseCounter<List<SearchResponseDto>>> searchNearByUser(
            @RequestParam double x,
            @RequestParam double y,
            @RequestParam double radius
    ){
        return ResponseEntity.ok()
                .body(new ResponseCounter<>(collectionService.getByDist(x,y,radius)));
    }

    //검색 데이터 API
    @GetMapping("/search")
    public ResponseEntity<ResponseCounter<List<SearchResponseDto>>> searchByKeyword(
            @RequestParam String keyword
    ){
        return ResponseEntity.ok()
                .body(new ResponseCounter<>(collectionService.getByAddress(keyword)));
    }

}
