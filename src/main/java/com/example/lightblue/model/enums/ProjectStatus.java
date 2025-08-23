package com.example.lightblue.model.enums;

public enum ProjectStatus {
    RECRUITING("모집중"),
    IN_PROGRESS("진행중"),
    COMPLETED("완료"),
    CANCELLED("취소"),
    PAUSED("일시중단");

    private final String description;

    ProjectStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}