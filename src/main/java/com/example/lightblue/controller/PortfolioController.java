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

@RestController
@RequestMapping("/api/portfolios")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PortfolioDTO>>> getAllPortfolios() {
        List<PortfolioDTO> portfolioDTOs = portfolioService.getAllPortfolios().stream()
                .map(PortfolioDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.onSuccess(portfolioDTOs));
    }
}
