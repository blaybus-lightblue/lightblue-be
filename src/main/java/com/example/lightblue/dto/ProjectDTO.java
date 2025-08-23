package com.example.lightblue.dto;

import com.example.lightblue.model.Project;
import com.example.lightblue.model.enums.ArtField;
import com.example.lightblue.model.enums.City;
import com.example.lightblue.model.enums.ProjectStatus;
import com.example.lightblue.model.enums.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Long id;
    private Long creatorId;
    private String creatorName;
    private String title;
    private ProjectType projectType;
    private String requirements;
    private ArtField primaryArtField;
    private ArtField secondaryArtField;
    private Integer recruitmentCount;
    private LocalDate startDate;
    private City activityCity;
    private Long expectedBudget;
    private String description;
    private String referenceUrl;
    private ProjectStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProjectDTO(Project project) {
        this.id = project.getId();
        this.creatorId = project.getCreator().getId();
        this.creatorName = project.getCreator().getUsername();
        this.title = project.getTitle();
        this.projectType = project.getProjectType();
        this.requirements = project.getRequirements();
        this.primaryArtField = project.getPrimaryArtField();
        this.secondaryArtField = project.getSecondaryArtField();
        this.recruitmentCount = project.getRecruitmentCount();
        this.startDate = project.getStartDate();
        this.activityCity = project.getActivityCity();
        this.expectedBudget = project.getExpectedBudget();
        this.description = project.getDescription();
        this.referenceUrl = project.getReferenceUrl();
        this.status = project.getStatus();
        this.createdAt = project.getCreatedAt();
        this.updatedAt = project.getUpdatedAt();
    }
}