/*
 * Copyright (c) 2024.
 * Created by Mykhailo Kovalenko
 */

package com.matchmetrics.security.entity.dto;

import com.matchmetrics.security.entity.Role;

public class UserGetDto {

    private String name;

    private String email;

    private String favouriteTeam;

    private Role role;

    public UserGetDto(String name, String email, String favouriteTeam, Role role) {
        this.name = name;
        this.email = email;
        this.favouriteTeam = favouriteTeam;
        this.role = role;
    }

    public UserGetDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFavouriteTeam() {
        return favouriteTeam;
    }

    public void setFavouriteTeam(String favouriteTeam) {
        this.favouriteTeam = favouriteTeam;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
