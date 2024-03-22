package com.matchmetrics.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.matchmetrics.json.serializer.CustomListSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "Team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @NotNull (message = "Team name should not be empty")
    private String name;

    @Column(name = "country")
    @NotNull (message = "Team country should not be empty")
    private String country;

    @Column(name = "elo")
    private float elo;

    @OneToMany(mappedBy = "homeTeam")
    @JsonSerialize(using = CustomListSerializer.class)
    private List<Match> homeMatches;

    @OneToMany(mappedBy = "awayTeam")
    @JsonSerialize(using = CustomListSerializer.class)
    private List<Match> awayMatches;

    public Team(String name, String country, float elo) {
        this.name = name;
        this.country = country;
        this.elo = elo;
    }
}
