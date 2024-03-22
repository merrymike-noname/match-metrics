package com.matchmetrics.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.matchmetrics.json.serializer.CustomMatchSerializer;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
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
    @JsonSerialize(using = CustomMatchSerializer.class)
    private Match match;

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
}
