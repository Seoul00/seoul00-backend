package com.backend.seoul00.domain.collection.dto;

import com.backend.seoul00.domain.collection.entity.Type;
import java.util.ArrayList;
import java.util.Arrays;
import lombok.Data;

import java.util.List;

@Data
public class SearchTypeRequestDto {
    private List<Type> type;

    public SearchTypeRequestDto(String type) {
        this.type = new ArrayList<>();
        if (type.isEmpty()) {
            this.type = Arrays.asList(Type.values());
        } else {
            for (Type t : Type.values()) {
                if (type.equals(t.name())) this.type.add(t);
            }
        }
    }
}
