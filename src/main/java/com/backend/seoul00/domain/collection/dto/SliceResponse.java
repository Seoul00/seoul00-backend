package com.backend.seoul00.domain.collection.dto;

import lombok.Data;
import org.springframework.data.domain.Slice;

import java.util.List;

@Data
public class SliceResponse<T> {
    private List<T> content;
    private boolean hasNext;

    public SliceResponse(Slice<T> contents) {
        this.content = contents.getContent();
        this.hasNext = contents.hasNext();
    }
}
