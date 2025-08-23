package com.example.lightblue.model.enums;

public enum ProjectType {
    PERFORMANCE("공연"),
    EXHIBITION("전시"),
    FESTIVAL("축제"),
    WORKSHOP("워크숍"),
    COLLABORATION("콜라보레이션"),
    COMMERCIAL("상업적 프로젝트"),
    EDUCATIONAL("교육"),
    COMMUNITY("커뮤니티"),
    DIGITAL("디지털 아트"),
    INSTALLATION("설치 미술"),
    OTHER("기타");

    private final String description;

    ProjectType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}