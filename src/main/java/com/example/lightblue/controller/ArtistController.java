package com.example.lightblue.controller;

import com.example.lightblue.dto.ArtistCreateRequest;
import com.example.lightblue.dto.ArtistDTO;
import com.example.lightblue.dto.ArtistUpdateRequest;
import com.example.lightblue.dto.PortfolioDTO;
import com.example.lightblue.global.ApiResponse;
import com.example.lightblue.model.Artist;
import com.example.lightblue.model.Portfolio;
import com.example.lightblue.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import com.example.lightblue.model.enums.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/artists")
@Tag(name = "Artist", description = "아티스트 관련 API")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @GetMapping
    @Operation(summary = "아티스트 검색", description = "활동 분야, 경력, 포트폴리오 유무로 아티스트를 검색합니다.")
    public ResponseEntity<ApiResponse<Page<ArtistDTO>>> searchArtists(
            @RequestParam(value = "city", required = false) City city,
            @RequestParam(value = "career", required = false) Integer career,
            @RequestParam(value = "hasPortfolios", required = false) Boolean hasPortfolios,
            Pageable pageable) {
        Page<ArtistDTO> artistDTOs = artistService.searchArtists(city, career, hasPortfolios, pageable)
                .map(ArtistDTO::new);
        return ResponseEntity.ok(ApiResponse.onSuccess(artistDTOs));
    }

    @GetMapping("/{id}")
    @Operation(summary = "아티스트 상세 조회", description = "특정 ID를 가진 아티스트의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<ArtistDTO>> getArtistById(@PathVariable Long id) {
        return artistService.getArtistById(id)
                .map(artist -> ResponseEntity.ok(ApiResponse.onSuccess(new ArtistDTO(artist))))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "아티스트 생성", description = "새로운 아티스트를 등록합니다.")
    public ResponseEntity<ApiResponse<ArtistDTO>> createArtist(@RequestBody ArtistCreateRequest artistRequest) {
        Artist createdArtist = artistService.createArtist(artistRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccess(new ArtistDTO(createdArtist)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ARTIST')")
    @Operation(summary = "아티스트 정보 수정", description = "기존 아티스트 정보를 수정합니다.")
    public ResponseEntity<ApiResponse<ArtistDTO>> updateArtist(@PathVariable Long id, @RequestBody ArtistUpdateRequest artistDetails) {
        Artist updatedArtist = artistService.updateArtist(id, artistDetails);
        return ResponseEntity.ok(ApiResponse.onSuccess(new ArtistDTO(updatedArtist)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ARTIST')")
    @Operation(summary = "아티스트 삭제", description = "특정 아티스트를 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteArtist(@PathVariable Long id) {
        artistService.deleteArtist(id);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    // Portfolio related endpoints
    @GetMapping("/{artistId}/portfolios")
    @Operation(summary = "아티스트의 포트폴리오 조회", description = "특정 아티스트의 모든 포트폴리오를 조회합니다.")
    public ResponseEntity<ApiResponse<List<PortfolioDTO>>> getPortfoliosByArtistId(@PathVariable Long artistId) {
        List<PortfolioDTO> portfolioDTOs = artistService.getPortfoliosByArtistId(artistId).stream()
                .map(PortfolioDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.onSuccess(portfolioDTOs));
    }

    @PostMapping(value = "/{artistId}/portfolios", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAuthority('ARTIST')")
    @Operation(summary = "아티스트에게 포트폴리오 추가", description = "특정 아티스트에게 새로운 포트폴리오를 추가합니다.")
    public ResponseEntity<ApiResponse<PortfolioDTO>> addPortfolioToArtist(@PathVariable Long artistId, @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        Portfolio createdPortfolio = artistService.addPortfolioToArtist(artistId, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccess(new PortfolioDTO(createdPortfolio)));
    }

    @PutMapping(value = "/{artistId}/portfolios/{portfolioId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAuthority('ARTIST')")
    @Operation(summary = "아티스트 포트폴리오 수정", description = "특정 아티스트의 포트폴리오를 수정합니다.")
    public ResponseEntity<ApiResponse<PortfolioDTO>> updatePortfolio(@PathVariable Long artistId, @PathVariable Long portfolioId, @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        Portfolio updatedPortfolio = artistService.updatePortfolio(portfolioId, files);
        return ResponseEntity.ok(ApiResponse.onSuccess(new PortfolioDTO(updatedPortfolio)));
    }

    @DeleteMapping("/{artistId}/portfolios/{portfolioId}")
    @PreAuthorize("hasAuthority('ARTIST')")
    @Operation(summary = "아티스트 포트폴리오 삭제", description = "특정 아티스트의 포트폴리오를 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> deletePortfolio(@PathVariable Long artistId, @PathVariable Long portfolioId) {
        artistService.deletePortfolio(portfolioId);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }
}
