package com.example.lightblue.controller;

import com.example.lightblue.dto.ArtistCreateRequest;
import com.example.lightblue.dto.ArtistDTO;
import com.example.lightblue.dto.ArtistUpdateRequest;
import com.example.lightblue.dto.PortfolioDTO;
import com.example.lightblue.global.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<ArtistDTO>>> getAllArtists() {
        List<ArtistDTO> artistDTOs = artistService.getAllArtists().stream()
                .map(ArtistDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.onSuccess(artistDTOs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ArtistDTO>> getArtistById(@PathVariable Long id) {
        return artistService.getArtistById(id)
                .map(artist -> ResponseEntity.ok(ApiResponse.onSuccess(new ArtistDTO(artist))))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ArtistDTO>> createArtist(@RequestBody ArtistCreateRequest artistRequest) {
        Artist createdArtist = artistService.createArtist(artistRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccess(new ArtistDTO(createdArtist)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ARTIST')")
    public ResponseEntity<ApiResponse<ArtistDTO>> updateArtist(@PathVariable Long id, @RequestBody ArtistUpdateRequest artistDetails) {
        Artist updatedArtist = artistService.updateArtist(id, artistDetails);
        return ResponseEntity.ok(ApiResponse.onSuccess(new ArtistDTO(updatedArtist)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ARTIST')")
    public ResponseEntity<ApiResponse<Void>> deleteArtist(@PathVariable Long id) {
        artistService.deleteArtist(id);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    // Portfolio related endpoints
    @GetMapping("/{artistId}/portfolios")
    public ResponseEntity<ApiResponse<List<PortfolioDTO>>> getPortfoliosByArtistId(@PathVariable Long artistId) {
        List<PortfolioDTO> portfolioDTOs = artistService.getPortfoliosByArtistId(artistId).stream()
                .map(PortfolioDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.onSuccess(portfolioDTOs));
    }

    @PostMapping(value = "/{artistId}/portfolios", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAuthority('ARTIST')")
    public ResponseEntity<ApiResponse<PortfolioDTO>> addPortfolioToArtist(@PathVariable Long artistId, @RequestPart("portfolioRequest") PortfolioRequest portfolioRequest, @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        Portfolio createdPortfolio = artistService.addPortfolioToArtist(artistId, portfolioRequest, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccess(new PortfolioDTO(createdPortfolio)));
    }

    @PutMapping(value = "/{artistId}/portfolios/{portfolioId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAuthority('ARTIST')")
    public ResponseEntity<ApiResponse<PortfolioDTO>> updatePortfolio(@PathVariable Long artistId, @PathVariable Long portfolioId, @RequestPart("portfolioRequest") PortfolioRequest portfolioRequest, @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        Portfolio updatedPortfolio = artistService.updatePortfolio(portfolioId, portfolioRequest, files);
        return ResponseEntity.ok(ApiResponse.onSuccess(new PortfolioDTO(updatedPortfolio)));
    }

    @DeleteMapping("/{artistId}/portfolios/{portfolioId}")
    @PreAuthorize("hasAuthority('ARTIST')")
    public ResponseEntity<ApiResponse<Void>> deletePortfolio(@PathVariable Long artistId, @PathVariable Long portfolioId) {
        artistService.deletePortfolio(portfolioId);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }
}
