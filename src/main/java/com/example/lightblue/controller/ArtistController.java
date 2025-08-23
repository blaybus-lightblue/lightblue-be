package com.example.lightblue.controller;

import com.example.lightblue.dto.ArtistCreateRequest;
import com.example.lightblue.dto.ArtistDTO;
import com.example.lightblue.dto.ArtistUpdateRequest;
import com.example.lightblue.dto.PortfolioDTO;
import com.example.lightblue.model.Artist;
import com.example.lightblue.model.Portfolio;
import com.example.lightblue.service.ArtistService;
import com.example.lightblue.dto.PortfolioRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @GetMapping
    public List<ArtistDTO> getAllArtists() {
        return artistService.getAllArtists().stream()
                .map(ArtistDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistDTO> getArtistById(@PathVariable Long id) {
        return artistService.getArtistById(id)
                .map(artist -> new ResponseEntity<>(new ArtistDTO(artist), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ArtistDTO> createArtist(@RequestBody ArtistCreateRequest artistRequest) {
        Artist createdArtist = artistService.createArtist(artistRequest);
        return new ResponseEntity<>(new ArtistDTO(createdArtist), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ARTIST')")
    public ResponseEntity<ArtistDTO> updateArtist(@PathVariable Long id, @RequestBody ArtistUpdateRequest artistDetails) {
        Artist updatedArtist = artistService.updateArtist(id, artistDetails);
        return new ResponseEntity<>(new ArtistDTO(updatedArtist), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ARTIST')")
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        artistService.deleteArtist(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Portfolio related endpoints
    @GetMapping("/{artistId}/portfolios")
    public List<PortfolioDTO> getPortfoliosByArtistId(@PathVariable Long artistId) {
        return artistService.getPortfoliosByArtistId(artistId).stream()
                .map(PortfolioDTO::new)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/{artistId}/portfolios", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAuthority('ARTIST')")
    public ResponseEntity<PortfolioDTO> addPortfolioToArtist(@PathVariable Long artistId, @RequestPart("portfolioRequest") PortfolioRequest portfolioRequest, @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        Portfolio createdPortfolio = artistService.addPortfolioToArtist(artistId, portfolioRequest, files);
        return new ResponseEntity<>(new PortfolioDTO(createdPortfolio), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{artistId}/portfolios/{portfolioId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAuthority('ARTIST')")
    public ResponseEntity<PortfolioDTO> updatePortfolio(@PathVariable Long artistId, @PathVariable Long portfolioId, @RequestPart("portfolioRequest") PortfolioRequest portfolioRequest, @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        // Ensure the portfolio belongs to the artist if needed, or handle in service
        Portfolio updatedPortfolio = artistService.updatePortfolio(portfolioId, portfolioRequest, files);
        return new ResponseEntity<>(new PortfolioDTO(updatedPortfolio), HttpStatus.OK);
    }

    @DeleteMapping("/{artistId}/portfolios/{portfolioId}")
    @PreAuthorize("hasAuthority('ARTIST')")
    public ResponseEntity<Void> deletePortfolio(@PathVariable Long artistId, @PathVariable Long portfolioId) {
        artistService.deletePortfolio(portfolioId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
