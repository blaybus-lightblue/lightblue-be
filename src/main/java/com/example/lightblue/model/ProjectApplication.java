package com.example.lightblue.model;

import com.example.lightblue.global.common.BaseEntity;
import com.example.lightblue.model.enums.ProjectApplicationStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "project_application")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProjectApplication extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectApplicationStatus status;

    public void updateStatus(ProjectApplicationStatus status) {
        this.status = status;
    }
}
