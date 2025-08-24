package com.example.lightblue.dto;

import com.example.lightblue.model.enums.ArtField;
import com.example.lightblue.model.enums.City;
import com.example.lightblue.model.enums.ProjectType;
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
    private ArtField jobField;
    private City city;
    private ProjectType activityField;
    private ProjectType desiredCollaborationField;
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
        this.city = artist.getCity();
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

    public ArtField getJobField() {
        return jobField;
    }

    public City getCity() {
        return city;
    }

    public ProjectType getActivityField() {
        return activityField;
    }

    public ProjectType getDesiredCollaborationField() {
        return desiredCollaborationField;
    }

    public String getIntroduction() {
        return introduction;
    }

    public List<PortfolioDTO> getPortfolios() {
        return portfolios;
    }
}