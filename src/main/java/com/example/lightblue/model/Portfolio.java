package com.example.lightblue.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "portfolio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @Column(nullable = true) // url is now optional
    private String url;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PortfolioFile> files = new ArrayList<>();

    public void addFile(PortfolioFile file) {
        files.add(file);
        file.setPortfolio(this);
    }

    public void removeFile(PortfolioFile file) {
        files.remove(file);
        file.setPortfolio(null);
    }
}
