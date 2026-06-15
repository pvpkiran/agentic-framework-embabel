package com.embabel.framework.repository;

import com.embabel.framework.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Integer> {

    Optional<Candidate> findByNameIgnoreCase(String name);
}
