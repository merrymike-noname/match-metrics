package com.matchmetrics.entity.dto.team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class TeamNameDto {
    @NotNull(message = "Team name should not be null")
    @NotBlank(message = "Team name should not be blank")
    @NotEmpty(message = "Team name should not be empty")
    private String name;

    public TeamNameDto(String name) {
        this.name = name;
    }

    public TeamNameDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamNameDto that = (TeamNameDto) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
