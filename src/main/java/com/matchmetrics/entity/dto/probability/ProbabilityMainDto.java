package com.matchmetrics.entity.dto.probability;

import java.util.Objects;

public class ProbabilityMainDto {
    private float homeTeamWin;
    private float draw;
    private float awayTeamWin;

    public ProbabilityMainDto(float homeTeamWin, float draw, float awayTeamWin) {
        this.homeTeamWin = homeTeamWin;
        this.draw = draw;
        this.awayTeamWin = awayTeamWin;
    }

    public ProbabilityMainDto() {
    }

    public float getHomeTeamWin() {
        return this.homeTeamWin;
    }

    public float getDraw() {
        return this.draw;
    }

    public float getAwayTeamWin() {
        return this.awayTeamWin;
    }

    public void setHomeTeamWin(float homeTeamWin) {
        this.homeTeamWin = homeTeamWin;
    }

    public void setDraw(float draw) {
        this.draw = draw;
    }

    public void setAwayTeamWin(float awayTeamWin) {
        this.awayTeamWin = awayTeamWin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProbabilityMainDto that = (ProbabilityMainDto) o;
        return Float.compare(homeTeamWin, that.homeTeamWin) == 0
                && Float.compare(draw, that.draw) == 0
                && Float.compare(awayTeamWin, that.awayTeamWin) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeTeamWin, draw, awayTeamWin);
    }
}
