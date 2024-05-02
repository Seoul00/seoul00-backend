package com.backend.seoul00.domain.collection.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Type {
    CLOTHES("의류"),
    MEDICINE("폐의약품"),
    BATTERY("폐건전지"),
    FLUORESCENT("폐형광등"),
    STREET_TRASH("가로 쓰레기통")
    ;

    private final String type;
}
