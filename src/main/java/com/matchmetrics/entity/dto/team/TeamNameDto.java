package com.matchmetrics.entity.dto.team;

import jakarta.validation.constraints.NotNull;

public class TeamNameDto {
    @NotNull(message = "Team name should not be empty")
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
}
