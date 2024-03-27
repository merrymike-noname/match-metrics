package com.matchmetrics.entity.dto.main;

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
}
