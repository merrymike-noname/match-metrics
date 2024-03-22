package com.matchmetrics.repository;

import com.matchmetrics.entity.Probability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProbabilityRepository extends JpaRepository<Probability, Integer> {
}
