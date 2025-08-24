package com.example.lightblue.dto;

import com.example.lightblue.model.enums.ProjectApplicationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectApplicationStatusUpdateRequest {
    private ProjectApplicationStatus status;
}
