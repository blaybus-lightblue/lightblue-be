package com.example.lightblue.repository;

import com.example.lightblue.model.ProjectApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectApplicationRepository extends JpaRepository<ProjectApplication, Long> {
    Optional<ProjectApplication> findByProjectIdAndArtistId(Long projectId, Long artistId);
}
