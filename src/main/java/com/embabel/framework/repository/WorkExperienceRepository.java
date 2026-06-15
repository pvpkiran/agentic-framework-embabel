package com.embabel.framework.repository;

import com.embabel.framework.entity.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Integer> {

    List<WorkExperience> findByCandidateIdOrderByStartDateDesc(Integer candidateId);

    @Query("SELECT w FROM WorkExperience w WHERE w.candidate.id = :candidateId AND LOWER(w.technologies) LIKE LOWER(CONCAT('%', :technology, '%'))")
    List<WorkExperience> findByTechnology(Integer candidateId, String technology);
}
