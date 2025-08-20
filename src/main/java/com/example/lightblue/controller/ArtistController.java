package com.example.lightblue.controller;

import com.example.lightblue.dto.ArtistDTO;
import com.example.lightblue.model.Artist;
import com.example.lightblue.model.Portfolio;
import com.example.lightblue.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @GetMapping
    public List<Artist> getAllArtists() {
        return artistService.getAllArtists();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistDTO> getArtistById(@PathVariable Long id) {
        return artistService.getArtistById(id)
                .map(artist -> new ResponseEntity<>(new ArtistDTO(artist), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist) {
        Artist createdArtist = artistService.createArtist(artist);
        return new ResponseEntity<>(createdArtist, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ARTIST')")
    public ResponseEntity<Artist> updateArtist(@PathVariable Long id, @RequestBody Artist artistDetails) {
        Artist updatedArtist = artistService.updateArtist(id, artistDetails);
        return new ResponseEntity<>(updatedArtist, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ARTIST')")
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        artistService.deleteArtist(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Portfolio related endpoints
    @GetMapping("/{artistId}/portfolios")
    public List<ArtistDTO.PortfolioDTO> getPortfoliosByArtistId(@PathVariable Long artistId) {
        return artistService.getPortfoliosByArtistId(artistId).stream()
                .map(ArtistDTO.PortfolioDTO::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/{artistId}/portfolios")
    @PreAuthorize("hasAuthority('ARTIST')")
    public ResponseEntity<Portfolio> addPortfolioToArtist(@PathVariable Long artistId, @RequestBody Portfolio portfolio) {
        Portfolio createdPortfolio = artistService.addPortfolioToArtist(artistId, portfolio);
        return new ResponseEntity<>(createdPortfolio, HttpStatus.CREATED);
    }

    @PutMapping("/{artistId}/portfolios/{portfolioId}")
    @PreAuthorize("hasAuthority('ARTIST')")
    public ResponseEntity<Portfolio> updatePortfolio(@PathVariable Long artistId, @PathVariable Long portfolioId, @RequestBody Portfolio portfolioDetails) {
        // Ensure the portfolio belongs to the artist if needed, or handle in service
        Portfolio updatedPortfolio = artistService.updatePortfolio(portfolioId, portfolioDetails);
        return new ResponseEntity<>(updatedPortfolio, HttpStatus.OK);
    }

    @DeleteMapping("/{artistId}/portfolios/{portfolioId}")
    @PreAuthorize("hasAuthority('ARTIST')")
    public ResponseEntity<Void> deletePortfolio(@PathVariable Long artistId, @PathVariable Long portfolioId) {
        artistService.deletePortfolio(portfolioId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
