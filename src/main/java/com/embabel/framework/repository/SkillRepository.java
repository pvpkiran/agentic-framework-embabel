package com.embabel.framework.repository;

import com.embabel.framework.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Integer> {

    List<Skill> findByCandidateIdAndCategoryIgnoreCase(Integer candidateId, String category);

    List<Skill> findByCandidateId(Integer candidateId);
}
