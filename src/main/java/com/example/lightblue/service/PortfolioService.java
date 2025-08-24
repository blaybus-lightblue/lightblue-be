package com.example.lightblue.service;

import com.example.lightblue.model.Portfolio;
import com.example.lightblue.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    public Page<Portfolio> getAllPortfolios(Pageable pageable) {
        return portfolioRepository.findAll(pageable);
    }
}