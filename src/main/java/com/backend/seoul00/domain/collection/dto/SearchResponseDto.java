package com.backend.seoul00.domain.collection.dto;

import com.backend.seoul00.domain.collection.entity.Type;
import lombok.Data;

@Data
public class SearchResponseDto {
    private Double longitude;
    private Double latitude;
    private String address;
    private Type type;

    public SearchResponseDto(Double longitude, Double latitude, String address, Type type) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.type = type;
    }
}
