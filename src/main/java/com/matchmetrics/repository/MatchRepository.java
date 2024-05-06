package com.matchmetrics.repository;

import com.matchmetrics.entity.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer>, JpaSpecificationExecutor<Match> {

    @EntityGraph(attributePaths = { "homeTeam", "awayTeam", "probability" })
    @Override
    Page<Match> findAll(Specification<Match> spec, Pageable pageable);

    @Query("SELECT m FROM Match m LEFT JOIN FETCH m.homeTeam LEFT JOIN FETCH m.awayTeam LEFT JOIN FETCH m.probability")
    @Override
    Page<Match> findAll(Pageable pageable);

    Optional<Match> findTopByHomeTeamNameAndAwayTeamNameOrderByDateDesc(String homeTeam, String awayTeam);

    Boolean existsByProbabilityId(int probabilityId);
}
