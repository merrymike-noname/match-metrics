package com.matchmetrics.entity.dto.main;

import com.matchmetrics.entity.dto.nested.TeamNestedDto;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class MatchMainDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private String league;

    private TeamNestedDto homeTeam;

    private TeamNestedDto awayTeam;

    private ProbabilityMainDto probability;

    public MatchMainDto(Date date, String league, TeamNestedDto homeTeam, TeamNestedDto awayTeam, ProbabilityMainDto probability) {
        this.date = date;
        this.league = league;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.probability = probability;
    }

    public MatchMainDto() {
    }

    public Date getDate() {
        return this.date;
    }

    public String getLeague() {
        return this.league;
    }

    public TeamNestedDto getHomeTeam() {
        return this.homeTeam;
    }

    public TeamNestedDto getAwayTeam() {
        return this.awayTeam;
    }

    public ProbabilityMainDto getProbability() {
        return this.probability;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public void setHomeTeam(TeamNestedDto homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(TeamNestedDto awayTeam) {
        this.awayTeam = awayTeam;
    }

    public void setProbability(ProbabilityMainDto probability) {
        this.probability = probability;
    }
}
