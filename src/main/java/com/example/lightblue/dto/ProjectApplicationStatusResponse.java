package com.example.lightblue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectApplicationStatusResponse {
    private List<ProjectApplicationDTO> directApplicants;
    private List<ProjectApplicationDTO> recommendedApplicants;
}
