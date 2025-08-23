package com.example.lightblue.service;

import com.example.lightblue.dto.ProjectCreateRequest;
import com.example.lightblue.dto.ProjectUpdateRequest;
import com.example.lightblue.model.Account;
import com.example.lightblue.model.Project;
import com.example.lightblue.model.enums.ArtField;
import com.example.lightblue.model.enums.City;
import com.example.lightblue.model.enums.ProjectStatus;
import com.example.lightblue.model.enums.ProjectType;
import com.example.lightblue.repository.AccountRepository;
import com.example.lightblue.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final AccountRepository accountRepository;

    public Project createProject(ProjectCreateRequest request, Long creatorId) {
        Account creator = accountRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("계정을 찾을 수 없습니다."));

        Project project = new Project();
        project.setCreator(creator);
        project.setTitle(request.getTitle());
        project.setProjectType(request.getProjectType());
        project.setRequirements(request.getRequirements());
        project.setPrimaryArtField(request.getPrimaryArtField());
        project.setSecondaryArtField(request.getSecondaryArtField());
        project.setRecruitmentCount(request.getRecruitmentCount());
        project.setStartDate(request.getStartDate());
        project.setActivityCity(request.getActivityCity());
        project.setExpectedBudget(request.getExpectedBudget());
        project.setDescription(request.getDescription());
        project.setReferenceUrl(request.getReferenceUrl());
        project.setStatus(ProjectStatus.RECRUITING);

        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Project> getProjectsWithPaging(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Project> getProjectsByStatus(ProjectStatus status) {
        return projectRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Project> getProjectsByCreator(Long creatorId) {
        return projectRepository.findByCreatorId(creatorId);
    }

    @Transactional(readOnly = true)
    public List<Project> getProjectsByType(ProjectType projectType) {
        return projectRepository.findByProjectType(projectType);
    }

    @Transactional(readOnly = true)
    public List<Project> getProjectsByArtField(ArtField artField) {
        return projectRepository.findByPrimaryArtFieldOrSecondaryArtField(artField, artField);
    }

    @Transactional(readOnly = true)
    public List<Project> getProjectsByCity(City city) {
        return projectRepository.findByActivityCity(city);
    }

    @Transactional(readOnly = true)
    public List<Project> getProjectsByStartDateAfter(LocalDate date) {
        return projectRepository.findByStartDateAfter(date);
    }

    @Transactional(readOnly = true)
    public List<Project> getProjectsByBudgetRange(Long minBudget, Long maxBudget) {
        return projectRepository.findByBudgetRange(minBudget, maxBudget);
    }

    @Transactional(readOnly = true)
    public List<Project> searchProjects(String keyword) {
        return projectRepository.findByKeyword(keyword);
    }

    public Project updateProject(Long id, ProjectUpdateRequest request, Long userId) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("프로젝트를 찾을 수 없습니다."));

        if (!project.getCreator().getId().equals(userId)) {
            throw new RuntimeException("프로젝트를 수정할 권한이 없습니다.");
        }

        if (request.getTitle() != null) {
            project.setTitle(request.getTitle());
        }
        if (request.getProjectType() != null) {
            project.setProjectType(request.getProjectType());
        }
        if (request.getRequirements() != null) {
            project.setRequirements(request.getRequirements());
        }
        if (request.getPrimaryArtField() != null) {
            project.setPrimaryArtField(request.getPrimaryArtField());
        }
        if (request.getSecondaryArtField() != null) {
            project.setSecondaryArtField(request.getSecondaryArtField());
        }
        if (request.getRecruitmentCount() != null) {
            project.setRecruitmentCount(request.getRecruitmentCount());
        }
        if (request.getStartDate() != null) {
            project.setStartDate(request.getStartDate());
        }
        if (request.getActivityCity() != null) {
            project.setActivityCity(request.getActivityCity());
        }
        if (request.getExpectedBudget() != null) {
            project.setExpectedBudget(request.getExpectedBudget());
        }
        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }
        if (request.getReferenceUrl() != null) {
            project.setReferenceUrl(request.getReferenceUrl());
        }
        if (request.getStatus() != null) {
            project.setStatus(request.getStatus());
        }

        return projectRepository.save(project);
    }

    public void deleteProject(Long id, Long userId) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("프로젝트를 찾을 수 없습니다."));

        if (!project.getCreator().getId().equals(userId)) {
            throw new RuntimeException("프로젝트를 삭제할 권한이 없습니다.");
        }

        projectRepository.delete(project);
    }
}