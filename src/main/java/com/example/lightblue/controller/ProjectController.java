package com.example.lightblue.controller;

import com.example.lightblue.dto.ProjectCreateRequest;
import com.example.lightblue.dto.ProjectDTO;
import com.example.lightblue.dto.ProjectUpdateRequest;
import com.example.lightblue.global.ApiResponse;
import com.example.lightblue.model.Project;
import com.example.lightblue.model.enums.ArtField;
import com.example.lightblue.model.enums.City;
import com.example.lightblue.model.enums.ProjectStatus;
import com.example.lightblue.model.enums.ProjectType;
import com.example.lightblue.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Project", description = "프로젝트 관련 API")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ARTIST', 'COMPANY')")
    @Operation(summary = "프로젝트 생성", description = "새로운 프로젝트를 생성합니다.")
    public ResponseEntity<ApiResponse<ProjectDTO>> createProject(
            @Valid @RequestBody ProjectCreateRequest request,
            Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = getUserIdFromAuthentication(authentication);
        
        Project project = projectService.createProject(request, userId);
        ProjectDTO projectDTO = new ProjectDTO(project);
        
        return ResponseEntity.ok(ApiResponse.onSuccess(projectDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "프로젝트 상세 조회", description = "특정 프로젝트의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<ProjectDTO>> getProject(@PathVariable Long id) {
        return projectService.getProjectById(id)
                .map(project -> ResponseEntity.ok(ApiResponse.onSuccess(new ProjectDTO(project))))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "프로젝트 목록 조회", description = "프로젝트 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<ProjectDTO>>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        Page<Project> projectPage = projectService.getProjectsWithPaging(pageable);
        List<ProjectDTO> projectDTOs = projectPage.getContent().stream()
                .map(ProjectDTO::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.onSuccess(projectDTOs));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ARTIST', 'COMPANY')")
    @Operation(summary = "프로젝트 수정", description = "기존 프로젝트 정보를 수정합니다.")
    public ResponseEntity<ApiResponse<ProjectDTO>> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectUpdateRequest request,
            Authentication authentication) {
        
        Long userId = getUserIdFromAuthentication(authentication);
        Project updatedProject = projectService.updateProject(id, request, userId);
        ProjectDTO projectDTO = new ProjectDTO(updatedProject);
        
        return ResponseEntity.ok(ApiResponse.onSuccess(projectDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ARTIST', 'COMPANY')")
    @Operation(summary = "프로젝트 삭제", description = "프로젝트를 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteProject(
            @PathVariable Long id,
            Authentication authentication) {
        
        Long userId = getUserIdFromAuthentication(authentication);
        projectService.deleteProject(id, userId);
        
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    @GetMapping("/search")
    @Operation(summary = "프로젝트 검색", description = "키워드로 프로젝트를 검색합니다.")
    public ResponseEntity<ApiResponse<List<ProjectDTO>>> searchProjects(
            @RequestParam String keyword) {
        
        List<Project> projects = projectService.searchProjects(keyword);
        List<ProjectDTO> projectDTOs = projects.stream()
                .map(ProjectDTO::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.onSuccess(projectDTOs));
    }

    @GetMapping("/filter/status")
    @Operation(summary = "상태별 프로젝트 조회", description = "프로젝트 상태별로 조회합니다.")
    public ResponseEntity<ApiResponse<List<ProjectDTO>>> getProjectsByStatus(
            @RequestParam ProjectStatus status) {
        
        List<Project> projects = projectService.getProjectsByStatus(status);
        List<ProjectDTO> projectDTOs = projects.stream()
                .map(ProjectDTO::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.onSuccess(projectDTOs));
    }

    @GetMapping("/filter/type")
    @Operation(summary = "유형별 프로젝트 조회", description = "프로젝트 유형별로 조회합니다.")
    public ResponseEntity<ApiResponse<List<ProjectDTO>>> getProjectsByType(
            @RequestParam ProjectType projectType) {
        
        List<Project> projects = projectService.getProjectsByType(projectType);
        List<ProjectDTO> projectDTOs = projects.stream()
                .map(ProjectDTO::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.onSuccess(projectDTOs));
    }

    @GetMapping("/filter/art-field")
    @Operation(summary = "예술분야별 프로젝트 조회", description = "예술분야별로 프로젝트를 조회합니다.")
    public ResponseEntity<ApiResponse<List<ProjectDTO>>> getProjectsByArtField(
            @RequestParam ArtField artField) {
        
        List<Project> projects = projectService.getProjectsByArtField(artField);
        List<ProjectDTO> projectDTOs = projects.stream()
                .map(ProjectDTO::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.onSuccess(projectDTOs));
    }

    @GetMapping("/filter/city")
    @Operation(summary = "지역별 프로젝트 조회", description = "활동지역별로 프로젝트를 조회합니다.")
    public ResponseEntity<ApiResponse<List<ProjectDTO>>> getProjectsByCity(
            @RequestParam City city) {
        
        List<Project> projects = projectService.getProjectsByCity(city);
        List<ProjectDTO> projectDTOs = projects.stream()
                .map(ProjectDTO::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.onSuccess(projectDTOs));
    }

    @GetMapping("/filter/budget")
    @Operation(summary = "예산범위별 프로젝트 조회", description = "예산 범위별로 프로젝트를 조회합니다.")
    public ResponseEntity<ApiResponse<List<ProjectDTO>>> getProjectsByBudgetRange(
            @RequestParam Long minBudget,
            @RequestParam Long maxBudget) {
        
        List<Project> projects = projectService.getProjectsByBudgetRange(minBudget, maxBudget);
        List<ProjectDTO> projectDTOs = projects.stream()
                .map(ProjectDTO::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.onSuccess(projectDTOs));
    }

    @GetMapping("/filter/start-date")
    @Operation(summary = "시작일 이후 프로젝트 조회", description = "특정 날짜 이후 시작하는 프로젝트를 조회합니다.")
    public ResponseEntity<ApiResponse<List<ProjectDTO>>> getProjectsByStartDateAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        List<Project> projects = projectService.getProjectsByStartDateAfter(date);
        List<ProjectDTO> projectDTOs = projects.stream()
                .map(ProjectDTO::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.onSuccess(projectDTOs));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ARTIST', 'COMPANY')")
    @Operation(summary = "내가 생성한 프로젝트 조회", description = "현재 사용자가 생성한 프로젝트를 조회합니다.")
    public ResponseEntity<ApiResponse<List<ProjectDTO>>> getMyProjects(Authentication authentication) {
        
        Long userId = getUserIdFromAuthentication(authentication);
        List<Project> projects = projectService.getProjectsByCreator(userId);
        List<ProjectDTO> projectDTOs = projects.stream()
                .map(ProjectDTO::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.onSuccess(projectDTOs));
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        return 1L;
    }
}