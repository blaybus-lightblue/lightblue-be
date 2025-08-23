package com.example.lightblue.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArtistCreateRequest {
    private String name;
    private String phone;
    private String email;
    private Integer career;
    private String jobField;
    private String activityArea;
    private String activityField;
    private String desiredCollaborationField;
    private String introduction;
    private Long accountId;
}
