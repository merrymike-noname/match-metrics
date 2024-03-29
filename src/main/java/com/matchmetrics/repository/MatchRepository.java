package com.matchmetrics.repository;

import com.matchmetrics.entity.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer>, JpaSpecificationExecutor<Match> {

    @EntityGraph(attributePaths = { "homeTeam", "awayTeam", "probability" })
    @Override
    Page<Match> findAll(Specification<Match> spec, Pageable pageable);
}
