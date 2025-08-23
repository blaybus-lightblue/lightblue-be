package com.example.lightblue.controller;

import com.example.lightblue.dto.PortfolioDTO;
import com.example.lightblue.global.ApiResponse;
import com.example.lightblue.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/portfolios")
@Tag(name = "Portfolio", description = "포트폴리오 관련 API")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping
    @Operation(summary = "모든 포트폴리오 조회", description = "모든 포트폴리오를 조회합니다.")
    public ResponseEntity<ApiResponse<List<PortfolioDTO>>> getAllPortfolios() {
        List<PortfolioDTO> portfolioDTOs = portfolioService.getAllPortfolios().stream()
                .map(PortfolioDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.onSuccess(portfolioDTOs));
    }
}
