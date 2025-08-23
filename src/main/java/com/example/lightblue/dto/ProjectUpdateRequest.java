package com.example.lightblue.dto;

import com.example.lightblue.model.enums.ArtField;
import com.example.lightblue.model.enums.City;
import com.example.lightblue.model.enums.ProjectStatus;
import com.example.lightblue.model.enums.ProjectType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUpdateRequest {
    
    @Size(max = 100, message = "프로젝트명은 100자를 초과할 수 없습니다")
    private String title;

    private ProjectType projectType;

    @Size(max = 500, message = "요구사항은 500자를 초과할 수 없습니다")
    private String requirements;

    private ArtField primaryArtField;

    private ArtField secondaryArtField;

    @Min(value = 1, message = "모집인원은 최소 1명입니다")
    @Max(value = 100, message = "모집인원은 최대 100명입니다")
    private Integer recruitmentCount;

    private LocalDate startDate;

    private City activityCity;

    @Min(value = 0, message = "예상 예산은 0원 이상이어야 합니다")
    private Long expectedBudget;

    @Size(min = 100, max = 1000, message = "프로젝트 상세 설명은 100자 이상 1000자 이하여야 합니다")
    private String description;

    @Size(max = 500, message = "참고자료는 500자를 초과할 수 없습니다")
    private String referenceUrl;

    private ProjectStatus status;
}