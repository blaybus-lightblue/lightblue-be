package com.example.lightblue.model.enums;

public enum ArtField {
    MUSIC("음악"),
    DANCE("무용"),
    THEATER("연극"),
    VISUAL_ARTS("시각예술"),
    LITERATURE("문학"),
    FILM("영화"),
    PHOTOGRAPHY("사진"),
    DESIGN("디자인"),
    CRAFT("공예"),
    DIGITAL_ART("디지털 아트"),
    FASHION("패션"),
    ARCHITECTURE("건축"),
    MULTIMEDIA("멀티미디어"),
    TRADITIONAL_ARTS("전통예술"),
    PERFORMANCE_ART("퍼포먼스 아트"),
    OTHER("기타");

    private final String description;

    ArtField(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}