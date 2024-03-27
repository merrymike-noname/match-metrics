package com.matchmetrics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table(name = "Team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @NotNull(message = "Team name should not be empty")
    private String name;

    @Column(name = "country")
    @NotNull(message = "Team country should not be empty")
    private String country;

    @Column(name = "elo")
    private float elo;

    @OneToMany(mappedBy = "homeTeam", fetch = FetchType.LAZY)
    private List<Match> homeMatches;

    @OneToMany(mappedBy = "awayTeam", fetch = FetchType.LAZY)
    private List<Match> awayMatches;

    public Team(String name, String country, float elo) {
        this.name = name;
        this.country = country;
        this.elo = elo;
    }

    public Team() {
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getCountry() {
        return this.country;
    }

    public float getElo() {
        return this.elo;
    }

    public List<Match> getHomeMatches() {
        return this.homeMatches;
    }

    public List<Match> getAwayMatches() {
        return this.awayMatches;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry (String country) {
        this.country = country;
    }

    public void setElo(float elo) {
        this.elo = elo;
    }

    public void setHomeMatches(List<Match> homeMatches) {
        this.homeMatches = homeMatches;
    }

    public void setAwayMatches(List<Match> awayMatches) {
        this.awayMatches = awayMatches;
    }

    public String toString() {
        return "Team(id=" + this.getId() +
                ", name=" + this.getName() +
                ", country=" + this.getCountry() +
                ", elo=" + this.getElo() +
                ", homeMatches=" + this.getHomeMatches() +
                ", awayMatches=" + this.getAwayMatches() + ")";
    }
}
