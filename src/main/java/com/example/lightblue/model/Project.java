package com.example.lightblue.model;

import com.example.lightblue.global.common.BaseEntity;
import com.example.lightblue.model.enums.ArtField;
import com.example.lightblue.model.enums.City;
import com.example.lightblue.model.enums.ProjectStatus;
import com.example.lightblue.model.enums.ProjectType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private Account creator;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "프로젝트명은 필수입니다")
    @Size(max = 100, message = "프로젝트명은 100자를 초과할 수 없습니다")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectType projectType;

    @Column(nullable = false, length = 500)
    @NotBlank(message = "요구사항은 필수입니다")
    @Size(max = 500, message = "요구사항은 500자를 초과할 수 없습니다")
    private String requirements;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArtField primaryArtField;

    @Enumerated(EnumType.STRING)
    private ArtField secondaryArtField;

    @Column(nullable = false)
    @Min(value = 1, message = "모집인원은 최소 1명입니다")
    @Max(value = 100, message = "모집인원은 최대 100명입니다")
    private Integer recruitmentCount;

    @Column(nullable = false)
    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private City activityCity;

    @Column(nullable = false)
    @Min(value = 0, message = "예상 예산은 0원 이상이어야 합니다")
    private Long expectedBudget;

    @Column(length = 1000)
    @Size(min = 100, max = 1000, message = "프로젝트 상세 설명은 100자 이상 1000자 이하여야 합니다")
    private String description;

    @Column(length = 500)
    @Size(max = 500, message = "참고자료는 500자를 초과할 수 없습니다")
    private String referenceUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status = ProjectStatus.RECRUITING;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectApplication> projectApplications = new ArrayList<>();
}