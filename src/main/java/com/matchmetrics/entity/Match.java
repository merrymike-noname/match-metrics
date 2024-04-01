package com.matchmetrics.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "Match")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @Column(name = "league")
    private String league;

    @ManyToOne
    @JoinColumn(name = "home_team_id", referencedColumnName = "id")
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team_id", referencedColumnName = "id")
    private Team awayTeam;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "probability_id", referencedColumnName = "id")
    private Probability probability;

    public Match(int id, Date date, String league, Team homeTeam, Team awayTeam, Probability probability) {
        this.id = id;
        this.date = date;
        this.league = league;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.probability = probability;
    }

    public Match() {
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public int getId() {
        return this.id;
    }

    public Date getDate() {
        return this.date;
    }

    public String getLeague() {
        return this.league;
    }

    public Team getHomeTeam() {
        return this.homeTeam;
    }

    public Team getAwayTeam() {
        return this.awayTeam;
    }

    public Probability getProbability() {
        return this.probability;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public void setProbability(Probability probability) {
        this.probability = probability;
    }

    public String toString() {
        return "Match(id=" + this.getId() +
                ", date=" + this.getDate() +
                ", league=" + this.getLeague() +
                ", homeTeam=" + this.getHomeTeam() +
                ", awayTeam=" + this.getAwayTeam() +
                ", probability=" + this.getProbability() + ")";
    }
}
