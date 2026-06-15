package com.embabel.framework.repository;

import com.embabel.framework.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    List<Project> findByCandidateId(Integer candidateId);

    @Query("SELECT p FROM Project p WHERE p.candidate.id = :candidateId AND LOWER(p.technologies) LIKE LOWER(CONCAT('%', :technology, '%'))")
    List<Project> findByTechnology(Integer candidateId, String technology);
}
