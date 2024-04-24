package com.matchmetrics.repository;

import com.matchmetrics.entity.Probability;
import com.matchmetrics.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProbabilityRepository extends JpaRepository<Probability, Integer> {

    Page<Probability> findAll(Specification<Probability> spec, Pageable pageable);

}
