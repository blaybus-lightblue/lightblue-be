package com.example.lightblue.dto;

import com.example.lightblue.model.enums.ProjectApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectApplicationDTO {
    private Long id;
    private Long projectId;
    private String projectName;
    private Long artistId;
    private String artistName;
    private ProjectApplicationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double matchScore;
}
