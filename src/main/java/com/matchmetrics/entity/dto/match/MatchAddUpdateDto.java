package com.matchmetrics.entity.dto.match;

import com.matchmetrics.entity.dto.probability.ProbabilityGetDto;
import com.matchmetrics.entity.dto.team.TeamNameDto;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Objects;

public class MatchAddUpdateDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String date;

    private String league;

    private TeamNameDto homeTeam;

    private TeamNameDto awayTeam;

    private ProbabilityGetDto probability;

    public MatchAddUpdateDto(String date, String league, TeamNameDto homeTeam, TeamNameDto awayTeam, ProbabilityGetDto probability) {
        this.date = date;
        this.league = league;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.probability = probability;
    }

    public MatchAddUpdateDto() {
    }

    public String getDate() {
        return this.date;
    }

    public String getLeague() {
        return this.league;
    }

    public TeamNameDto getHomeTeam() {
        return this.homeTeam;
    }

    public TeamNameDto getAwayTeam() {
        return this.awayTeam;
    }

    public ProbabilityGetDto getProbability() {
        return this.probability;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public void setHomeTeam(TeamNameDto homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(TeamNameDto awayTeam) {
        this.awayTeam = awayTeam;
    }

    public void setProbability(ProbabilityGetDto probability) {
        this.probability = probability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchAddUpdateDto that = (MatchAddUpdateDto) o;
        return Objects.equals(date, that.date)
                && Objects.equals(league, that.league)
                && Objects.equals(homeTeam, that.homeTeam)
                && Objects.equals(awayTeam, that.awayTeam)
                && Objects.equals(probability, that.probability);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, league, homeTeam, awayTeam, probability);
    }
}
