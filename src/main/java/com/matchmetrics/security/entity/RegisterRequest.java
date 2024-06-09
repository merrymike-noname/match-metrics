package com.matchmetrics.security.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class RegisterRequest {
    @NotNull(message = "Name should not be null")
    @NotBlank(message = "Name should not be blank")
    @NotEmpty(message = "Name should not be empty")
    private String name;

    @Email(message = "Email should have valid format")
    @NotNull(message = "Email should not be null")
    @NotBlank(message = "Email should not be blank")
    @NotEmpty(message = "Email should not be empty")
    private String email;

    @NotNull(message = "Password should not be null")
    @NotBlank(message = "Password should not be blank")
    @NotEmpty(message = "Password should not be empty")
    private String password;

    @NotNull(message = "Favourite team should not be null")
    @NotBlank(message = "Favourite team should not be blank")
    @NotEmpty(message = "Favourite team should not be empty")
    private String favouriteTeam;

    public RegisterRequest(String name, String email, String password, String favouriteTeam) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.favouriteTeam = favouriteTeam;
    }

    public RegisterRequest() {
    }

    public static RegisterRequestBuilder builder() {
        return new RegisterRequestBuilder();
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getFavouriteTeam() {
        return this.favouriteTeam;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFavouriteTeam(String favouriteTeam) {
        this.favouriteTeam = favouriteTeam;
    }


    protected boolean canEqual(final Object other) {
        return other instanceof RegisterRequest;
    }


    public String toString() {
        return "RegisterRequest(name=" + this.getName() + ", email=" + this.getEmail() + ", password=" + this.getPassword() + ", favouriteTeamId=" + this.getFavouriteTeam() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterRequest that = (RegisterRequest) o;
        return Objects.equals(name, that.name) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(favouriteTeam, that.favouriteTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, password, favouriteTeam);
    }

    public static class RegisterRequestBuilder {
        private String name;
        private String email;
        private String password;
        private String favouriteTeam;

        RegisterRequestBuilder() {
        }

        public RegisterRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public RegisterRequestBuilder email(String email) {
            this.email = email;
            return this;
        }

        public RegisterRequestBuilder password(String password) {
            this.password = password;
            return this;
        }

        public RegisterRequestBuilder favouriteTeam(String favouriteTeam) {
            this.favouriteTeam = favouriteTeam;
            return this;
        }

        public RegisterRequest build() {
            return new RegisterRequest(this.name, this.email, this.password, this.favouriteTeam);
        }

        public String toString() {
            return "RegisterRequest.RegisterRequestBuilder(name=" + this.name + ", email=" + this.email + ", password=" + this.password + ", favouriteTeamId=" + this.favouriteTeam + ")";
        }
    }
}
