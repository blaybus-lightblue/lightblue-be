package com.example.lightblue.service;

import com.example.lightblue.dto.ProjectApplicationDTO;
import com.example.lightblue.dto.ProjectApplicationStatusResponse;
import com.example.lightblue.global.code.status.ErrorStatus;
import com.example.lightblue.global.exception.GeneralException;
import com.example.lightblue.model.Artist;
import com.example.lightblue.model.Project;
import com.example.lightblue.model.ProjectApplication;
import com.example.lightblue.model.enums.ProjectApplicationStatus;
import com.example.lightblue.repository.ArtistRepository;
import com.example.lightblue.repository.ProjectApplicationRepository;
import com.example.lightblue.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectApplicationService {

    private final ProjectRepository projectRepository;
    private final ArtistRepository artistRepository;
    private final ProjectApplicationRepository projectApplicationRepository;

    @Transactional
    public ProjectApplicationDTO applyForProject(Long projectId, Long artistId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PROJECT_NOT_FOUND));

        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ARTIST_NOT_FOUND));

        // Check for duplicate application
        Optional<ProjectApplication> existingApplication = projectApplicationRepository.findByProjectIdAndArtistId(projectId, artistId);
        if (existingApplication.isPresent()) {
            throw new GeneralException(ErrorStatus.PROJECT_APPLICATION_ALREADY_EXISTS);
        }

        ProjectApplication projectApplication = ProjectApplication.builder()
                .project(project)
                .artist(artist)
                .status(ProjectApplicationStatus.PENDING)
                .build();

        projectApplicationRepository.save(projectApplication);

        return ProjectApplicationDTO.builder()
                .id(projectApplication.getId())
                .projectId(project.getId())
                .projectName(project.getTitle())
                .artistId(artist.getId())
                .artistName(artist.getName())
                .status(projectApplication.getStatus())
                .createdAt(projectApplication.getCreatedAt())
                .updatedAt(projectApplication.getUpdatedAt())
                .build();
    }

    public ProjectApplicationDTO getProjectApplicationById(Long applicationId) {
        ProjectApplication application = projectApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PROJECT_APPLICATION_NOT_FOUND));

        return ProjectApplicationDTO.builder()
                .id(application.getId())
                .projectId(application.getProject().getId())
                .projectName(application.getProject().getTitle())
                .artistId(application.getArtist().getId())
                .artistName(application.getArtist().getName())
                .status(application.getStatus())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }

    public List<ProjectApplicationDTO> getApplicationsForProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PROJECT_NOT_FOUND));

        return project.getProjectApplications().stream()
                .map(application -> ProjectApplicationDTO.builder()
                        .id(application.getId())
                        .projectId(application.getProject().getId())
                        .projectName(application.getProject().getTitle())
                        .artistId(application.getArtist().getId())
                        .artistName(application.getArtist().getName())
                        .status(application.getStatus())
                        .createdAt(application.getCreatedAt())
                        .updatedAt(application.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ProjectApplicationDTO> getApplicationsByArtist(Long artistId) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ARTIST_NOT_FOUND));

        return artist.getProjectApplications().stream()
                .map(application -> ProjectApplicationDTO.builder()
                        .id(application.getId())
                        .projectId(application.getProject().getId())
                        .projectName(application.getProject().getTitle())
                        .artistId(application.getArtist().getId())
                        .artistName(application.getArtist().getName())
                        .status(application.getStatus())
                        .createdAt(application.getCreatedAt())
                        .updatedAt(application.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjectApplicationDTO updateApplicationStatus(Long applicationId, ProjectApplicationStatus newStatus) {
        ProjectApplication application = projectApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PROJECT_APPLICATION_NOT_FOUND));

        application.updateStatus(newStatus);
        projectApplicationRepository.save(application);

        return ProjectApplicationDTO.builder()
                .id(application.getId())
                .projectId(application.getProject().getId())
                .projectName(application.getProject().getTitle())
                .artistId(application.getArtist().getId())
                .artistName(application.getArtist().getName())
                .status(application.getStatus())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }

    public ProjectApplicationStatusResponse getProjectApplicationStatus(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PROJECT_NOT_FOUND));

        List<ProjectApplication> allApplications = project.getProjectApplications();

        List<ProjectApplicationDTO> directApplicants = allApplications.stream()
                .map(this::mapToProjectApplicationDTO)
                .collect(Collectors.toList());

        List<ProjectApplicationDTO> recommendedApplicants = allApplications.stream()
                .map(application -> {
                    double score = calculateMatchScore(project, application.getArtist());
                    return mapToProjectApplicationDTOWithScore(application, score);
                })
                .sorted(Comparator.comparing(ProjectApplicationDTO::getMatchScore).reversed())
                .collect(Collectors.toList());

        return ProjectApplicationStatusResponse.builder()
                .directApplicants(directApplicants)
                .recommendedApplicants(recommendedApplicants)
                .build();
    }

    private double calculateMatchScore(Project project, Artist artist) {
        double score = 0.0;

        // ArtField matching
        if (project.getPrimaryArtField() != null && project.getPrimaryArtField().equals(artist.getJobField())) {
            score += 5.0; // High match
        } else if (project.getSecondaryArtField() != null && project.getSecondaryArtField().equals(artist.getJobField())) {
            score += 3.0; // Medium match
        }

        // ProjectType matching
        if (project.getProjectType() != null && (project.getProjectType().equals(artist.getActivityField()) || project.getProjectType().equals(artist.getDesiredCollaborationField()))) {
            score += 4.0;
        }

        // City matching
        if (project.getActivityCity() != null && project.getActivityCity().equals(artist.getCity())) {
            score += 2.0;
        }

        // Career (simple example: higher career is better)
        if (artist.getCareer() != null) {
            score += Math.min(artist.getCareer(), 10) * 0.5; // Max 5 points for career up to 10 years
        }

        // TODO: Add more sophisticated matching logic (e.g., text analysis for requirements/introduction)

        return score;
    }

    private ProjectApplicationDTO mapToProjectApplicationDTO(ProjectApplication application) {
        return ProjectApplicationDTO.builder()
                .id(application.getId())
                .projectId(application.getProject().getId())
                .projectName(application.getProject().getTitle())
                .artistId(application.getArtist().getId())
                .artistName(application.getArtist().getName())
                .status(application.getStatus())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }

    private ProjectApplicationDTO mapToProjectApplicationDTOWithScore(ProjectApplication application, double score) {
        return ProjectApplicationDTO.builder()
                .id(application.getId())
                .projectId(application.getProject().getId())
                .projectName(application.getProject().getTitle())
                .artistId(application.getArtist().getId())
                .artistName(application.getArtist().getName())
                .status(application.getStatus())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .matchScore(score)
                .build();
    }
}
