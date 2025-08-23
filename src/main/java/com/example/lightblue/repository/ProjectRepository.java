package com.example.lightblue.repository;

import com.example.lightblue.model.Project;
import com.example.lightblue.model.enums.ArtField;
import com.example.lightblue.model.enums.City;
import com.example.lightblue.model.enums.ProjectStatus;
import com.example.lightblue.model.enums.ProjectType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    List<Project> findByStatus(ProjectStatus status);
    
    List<Project> findByCreatorId(Long creatorId);
    
    List<Project> findByProjectType(ProjectType projectType);
    
    List<Project> findByPrimaryArtFieldOrSecondaryArtField(ArtField primaryArtField, ArtField secondaryArtField);
    
    List<Project> findByActivityCity(City city);
    
    List<Project> findByStartDateAfter(LocalDate date);
    
    @Query("SELECT p FROM Project p WHERE p.expectedBudget BETWEEN :minBudget AND :maxBudget")
    List<Project> findByBudgetRange(@Param("minBudget") Long minBudget, @Param("maxBudget") Long maxBudget);
    
    @Query("SELECT p FROM Project p WHERE p.title LIKE %:keyword% OR p.description LIKE %:keyword%")
    List<Project> findByKeyword(@Param("keyword") String keyword);
    
    Page<Project> findAll(Pageable pageable);
    
    Page<Project> findByStatus(ProjectStatus status, Pageable pageable);
}