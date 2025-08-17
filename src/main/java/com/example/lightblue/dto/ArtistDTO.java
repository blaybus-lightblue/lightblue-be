package com.example.lightblue.dto;

import com.example.lightblue.model.Artist;
import com.example.lightblue.model.Portfolio;

import java.util.List;
import java.util.stream.Collectors;

public class ArtistDTO {
    private Long id;
    private String name;
    private List<PortfolioDTO> portfolios;

    public ArtistDTO(Artist artist) {
        this.id = artist.getId();
        this.name = artist.getName();
        this.portfolios = artist.getPortfolios().stream()
                .map(PortfolioDTO::new)
                .collect(Collectors.toList());
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<PortfolioDTO> getPortfolios() {
        return portfolios;
    }

    public static class PortfolioDTO {
        private Long id;
        private String url;

        public PortfolioDTO(Portfolio portfolio) {
            this.id = portfolio.getId();
            this.url = portfolio.getUrl();
        }

        // Getters
        public Long getId() {
            return id;
        }

        public String getUrl() {
            return url;
        }
    }
}
