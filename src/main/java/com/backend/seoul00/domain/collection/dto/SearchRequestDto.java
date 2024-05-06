package com.backend.seoul00.domain.collection.dto;

import lombok.Data;

@Data
public class SearchRequestDto {

    private String query;
    private String type;

    private int page;
    private int size;

    public int getPage() {
        return page - 1;
    }
}
