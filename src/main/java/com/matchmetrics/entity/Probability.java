package com.matchmetrics.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Probability")
public class Probability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "home_team_win")
    private float homeTeamWin;

    @Column(name = "draw")
    private float draw;

    @Transient
    private float awayTeamWin;

    @OneToOne(mappedBy = "probability")
    private Match match;

    public Probability() {
    }

    public float getAwayTeamWin() {
        return 1 - (homeTeamWin + draw);
    }

    public void setAwayTeamWin(float homeTeamWin, float draw) {
        awayTeamWin = 1 - (homeTeamWin + draw);
    }

    public void setMatch(Match match) {
        this.match = match;
        match.setProbability(this);
    }

    public Probability(float homeTeamWin, float draw, float awayTeamWin) {
        this.homeTeamWin = homeTeamWin;
        this.draw = draw;
        this.awayTeamWin = awayTeamWin;
    }

    public int getId() {
        return this.id;
    }

    public float getHomeTeamWin() {
        return this.homeTeamWin;
    }

    public float getDraw() {
        return this.draw;
    }

    public Match getMatch() {
        return this.match;
    }

    public void setId(int id) {
        this.id = id;
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

    public String toString() {
        return "Probability(id=" + this.getId() +
                ", homeTeamWin=" + this.getHomeTeamWin() +
                ", draw=" + this.getDraw() +
                ", awayTeamWin=" + this.getAwayTeamWin() +
                ", match=" + this.getMatch() + ")";
    }
}
