package com.matchmetrics.repository;

import com.matchmetrics.entity.Probability;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProbabilityRepository extends JpaRepository<Probability, Integer>, JpaSpecificationExecutor<Probability> {
    @Override
    Page<Probability> findAll(Specification<Probability> spec, Pageable pageable);

    @Query("SELECT p FROM Probability p LEFT JOIN FETCH p.match")
    @Override
    Page<Probability> findAll(Pageable pageable);
}
