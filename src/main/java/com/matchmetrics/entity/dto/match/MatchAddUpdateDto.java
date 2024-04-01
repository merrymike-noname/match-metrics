package com.matchmetrics.entity.dto.match;

import com.matchmetrics.entity.dto.probability.ProbabilityMainDto;
import com.matchmetrics.entity.dto.team.TeamNameDto;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class MatchAddUpdateDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private String league;

    private TeamNameDto homeTeam;

    private TeamNameDto awayTeam;

    private ProbabilityMainDto probability;

    public MatchAddUpdateDto(Date date, String league, TeamNameDto homeTeam, TeamNameDto awayTeam, ProbabilityMainDto probability) {
        this.date = date;
        this.league = league;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.probability = probability;
    }

    public MatchAddUpdateDto() {
    }

    public Date getDate() {
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

    public ProbabilityMainDto getProbability() {
        return this.probability;
    }

    public void setDate(Date date) {
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

    public void setProbability(ProbabilityMainDto probability) {
        this.probability = probability;
    }
}
