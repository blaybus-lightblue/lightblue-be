package com.example.lightblue.dto;

import com.example.lightblue.model.Artist;
import java.util.List;
import java.util.stream.Collectors;

public class ArtistDTO {
    private Long id;
    private Long accountId;
    private String name;
    private String phone;
    private String email;
    private Integer career;
    private String jobField;
    private String activityArea;
    private String activityField;
    private String desiredCollaborationField;
    private String introduction;
    private List<PortfolioDTO> portfolios;

    public ArtistDTO(Artist artist) {
        this.id = artist.getId();
        this.accountId = artist.getAccount().getId();
        this.name = artist.getName();
        this.phone = artist.getPhone();
        this.email = artist.getEmail();
        this.career = artist.getCareer();
        this.jobField = artist.getJobField();
        this.activityArea = artist.getActivityArea();
        this.activityField = artist.getActivityField();
        this.desiredCollaborationField = artist.getDesiredCollaborationField();
        this.introduction = artist.getIntroduction();
        this.portfolios = artist.getPortfolios().stream()
                .map(PortfolioDTO::new) // This will now use the top-level PortfolioDTO
                .collect(Collectors.toList());
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Integer getCareer() {
        return career;
    }

    public String getJobField() {
        return jobField;
    }

    public String getActivityArea() {
        return activityArea;
    }

    public String getActivityField() {
        return activityField;
    }

    public String getDesiredCollaborationField() {
        return desiredCollaborationField;
    }

    public String getIntroduction() {
        return introduction;
    }

    public List<PortfolioDTO> getPortfolios() {
        return portfolios;
    }
}