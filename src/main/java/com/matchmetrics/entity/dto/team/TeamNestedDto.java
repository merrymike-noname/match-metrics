package com.matchmetrics.entity.dto.team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class TeamNestedDto {
    @NotNull(message = "Team name should not be null")
    @NotBlank(message = "Team name should not be blank")
    @NotEmpty(message = "Team name should not be empty")
    private String name;

    @NotNull(message = "Team country should not be null")
    @NotBlank(message = "Team country should not be blank")
    @NotEmpty(message = "Team country should not be empty")
    private String country;

    private float elo;

    public TeamNestedDto(
            String name,
            String country, float elo) {
        this.name = name;
        this.country = country;
        this.elo = elo;
    }

    public TeamNestedDto() {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setElo(float elo) {
        this.elo = elo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamNestedDto that = (TeamNestedDto) o;
        return Float.compare(elo, that.elo) == 0
                && Objects.equals(name, that.name)
                && Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, country, elo);
    }
}
