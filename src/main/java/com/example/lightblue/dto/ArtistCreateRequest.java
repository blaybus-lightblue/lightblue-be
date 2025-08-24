package com.example.lightblue.dto;

import com.example.lightblue.model.enums.ArtField;
import com.example.lightblue.model.enums.City;
import com.example.lightblue.model.enums.ProjectType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArtistCreateRequest {
    private String name;
    private String phone;
    private String email;
    private Integer career;
    private ArtField jobField;
    private City city;
    private ProjectType activityField;
    private ProjectType desiredCollaborationField;
    private String introduction;
    
}
