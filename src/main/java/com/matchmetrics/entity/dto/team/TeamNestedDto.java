package com.matchmetrics.entity.dto.team;

import jakarta.validation.constraints.NotNull;

public class TeamNestedDto {
    @NotNull(message = "Team name should not be empty")
    private String name;

    @NotNull(message = "Team country should not be empty")
    private String country;

    private float elo;

    public TeamNestedDto(@NotNull(message = "Team name should not be empty") String name, @NotNull(message = "Team country should not be empty") String country, float elo) {
        this.name = name;
        this.country = country;
        this.elo = elo;
    }

    public TeamNestedDto() {
    }

    public @NotNull(message = "Team name should not be empty") String getName() {
        return this.name;
    }

    public @NotNull(message = "Team country should not be empty") String getCountry() {
        return this.country;
    }

    public float getElo() {
        return this.elo;
    }

    public void setName(@NotNull(message = "Team name should not be empty") String name) {
        this.name = name;
    }

    public void setCountry(@NotNull(message = "Team country should not be empty") String country) {
        this.country = country;
    }

    public void setElo(float elo) {
        this.elo = elo;
    }
}
