package com.matchmetrics.entity.dto.team;

import com.matchmetrics.entity.dto.match.MatchListDto;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

public class TeamGetDto {
    private String name;

    private String country;

    private float elo;

    private List<MatchListDto> homeMatches;

    private List<MatchListDto> awayMatches;

    public TeamGetDto(String name, String country, float elo, List<MatchListDto> homeMatches, List<MatchListDto> awayMatches) {
        this.name = name;
        this.country = country;
        this.elo = elo;
        this.homeMatches = homeMatches;
        this.awayMatches = awayMatches;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getElo() {
        return elo;
    }

    public void setElo(float elo) {
        this.elo = elo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<MatchListDto> getHomeMatches() {
        return homeMatches;
    }

    public void setHomeMatches(List<MatchListDto> homeMatches) {
        this.homeMatches = homeMatches;
    }

    public List<MatchListDto> getAwayMatches() {
        return awayMatches;
    }

    public void setAwayMatches(List<MatchListDto> awayMatches) {
        this.awayMatches = awayMatches;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamGetDto that = (TeamGetDto) o;
        return Float.compare(that.elo, elo) == 0 &&
                Objects.equals(name, that.name) &&
                Objects.equals(country, that.country) &&
                Objects.equals(homeMatches, that.homeMatches) &&
                Objects.equals(awayMatches, that.awayMatches);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, country, elo, homeMatches, awayMatches);
    }
}
