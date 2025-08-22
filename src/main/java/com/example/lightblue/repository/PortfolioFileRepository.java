package com.example.lightblue.repository;

import com.example.lightblue.model.PortfolioFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioFileRepository extends JpaRepository<PortfolioFile, Long> {
}
