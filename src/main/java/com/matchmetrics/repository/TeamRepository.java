package com.matchmetrics.repository;

import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    Optional<Team> findTeamByName(String name);
    boolean existsByName(String name);
}
