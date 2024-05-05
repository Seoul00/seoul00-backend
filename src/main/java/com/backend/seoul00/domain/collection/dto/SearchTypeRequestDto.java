package com.backend.seoul00.domain.collection.dto;

import com.backend.seoul00.domain.collection.entity.Type;
import lombok.Data;

import java.util.List;

@Data
public class SearchTypeRequestDto {

    private List<Type> types;
}
