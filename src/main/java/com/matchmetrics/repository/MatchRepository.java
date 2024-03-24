package com.matchmetrics.repository;

import com.matchmetrics.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {

    @Query("SELECT match FROM Match match WHERE match.homeTeam.name = :teamName OR match.awayTeam.name = :teamName")
    List<Match> findMatchesByTeam(@Param("teamName") String teamName);

    @Query("SELECT match FROM Match match WHERE match.homeTeam.name = :teamName")
    List<Match> findHomeMatchesByTeam(@Param("teamName") String teamName);

    @Query("SELECT match FROM Match match WHERE match.awayTeam.name = :teamName")
    List<Match> findAwayMatchesByTeam(@Param("teamName") String teamName);
}
