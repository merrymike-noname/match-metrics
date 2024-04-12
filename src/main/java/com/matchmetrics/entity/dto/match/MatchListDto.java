package com.matchmetrics.entity.dto.match;

import com.matchmetrics.entity.dto.probability.ProbabilityMainDto;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Objects;

public class MatchListDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String date;

    private String league;

    private ProbabilityMainDto probability;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ProbabilityMainDto getProbability() {
        return probability;
    }

    public void setProbability(ProbabilityMainDto probability) {
        this.probability = probability;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public MatchListDto(String date, String league, ProbabilityMainDto probability) {
        this.date = date;
        this.league = league;
        this.probability = probability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchListDto that = (MatchListDto) o;
        return Objects.equals(date, that.date) &&
                Objects.equals(league, that.league) &&
                Objects.equals(probability, that.probability);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, league, probability);
    }
}
