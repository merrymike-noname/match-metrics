package com.matchmetrics.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
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

    @OneToOne
    @JoinColumn(name = "probability_id", referencedColumnName = "id")
    private Probability probability;

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
        homeTeam.getHomeMatches().add(this);
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
        awayTeam.getAwayMatches().add(this);
    }
}
