/*
 * Copyright (c) 2024.
 * Created by Mykhailo Kovalenko
 */

package com.matchmetrics.security.entity;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UserUpdateRequest {
    @NotNull(message = "Name should not be null")
    @NotBlank(message = "Name should not be blank")
    @NotEmpty(message = "Name should not be empty")
    private String name;

    @Email(message = "Email should have valid format")
    @NotNull(message = "Email should not be null")
    @NotBlank(message = "Email should not be blank")
    @NotEmpty(message = "Email should not be empty")
    private String email;

    private String password;

    @NotNull(message = "Favourite team should not be null")
    @NotBlank(message = "Favourite team should not be blank")
    @NotEmpty(message = "Favourite team should not be empty")
    private String favouriteTeam;

    UserUpdateRequest(@NotNull(message = "Name should not be null") @NotBlank(message = "Name should not be blank") @NotEmpty(message = "Name should not be empty") String name, @Email(message = "Email should have valid format") @NotNull(message = "Email should not be null") @NotBlank(message = "Email should not be blank") @NotEmpty(message = "Email should not be empty") String email, String password, @NotNull(message = "Favourite team should not be null") @NotBlank(message = "Favourite team should not be blank") @NotEmpty(message = "Favourite team should not be empty") String favouriteTeam) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.favouriteTeam = favouriteTeam;
    }

    public static UserUpdateRequestBuilder builder() {
        return new UserUpdateRequestBuilder();
    }

    public @NotNull(message = "Name should not be null") @NotBlank(message = "Name should not be blank") @NotEmpty(message = "Name should not be empty") String getName() {
        return this.name;
    }

    public @Email(message = "Email should have valid format") @NotNull(message = "Email should not be null") @NotBlank(message = "Email should not be blank") @NotEmpty(message = "Email should not be empty") String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public @NotNull(message = "Favourite team should not be null") @NotBlank(message = "Favourite team should not be blank") @NotEmpty(message = "Favourite team should not be empty") String getFavouriteTeam() {
        return this.favouriteTeam;
    }

    public void setName(@NotNull(message = "Name should not be null") @NotBlank(message = "Name should not be blank") @NotEmpty(message = "Name should not be empty") String name) {
        this.name = name;
    }

    public void setEmail(@Email(message = "Email should have valid format") @NotNull(message = "Email should not be null") @NotBlank(message = "Email should not be blank") @NotEmpty(message = "Email should not be empty") String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFavouriteTeam(@NotNull(message = "Favourite team should not be null") @NotBlank(message = "Favourite team should not be blank") @NotEmpty(message = "Favourite team should not be empty") String favouriteTeam) {
        this.favouriteTeam = favouriteTeam;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof UserUpdateRequest)) return false;
        final UserUpdateRequest other = (UserUpdateRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$email = this.getEmail();
        final Object other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) return false;
        final Object this$password = this.getPassword();
        final Object other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) return false;
        final Object this$favouriteTeam = this.getFavouriteTeam();
        final Object other$favouriteTeam = other.getFavouriteTeam();
        if (this$favouriteTeam == null ? other$favouriteTeam != null : !this$favouriteTeam.equals(other$favouriteTeam))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof UserUpdateRequest;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $email = this.getEmail();
        result = result * PRIME + ($email == null ? 43 : $email.hashCode());
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        final Object $favouriteTeam = this.getFavouriteTeam();
        result = result * PRIME + ($favouriteTeam == null ? 43 : $favouriteTeam.hashCode());
        return result;
    }

    public String toString() {
        return "UserUpdateRequest(name=" + this.getName() + ", email=" + this.getEmail() + ", password=" + this.getPassword() + ", favouriteTeam=" + this.getFavouriteTeam() + ")";
    }

    public static class UserUpdateRequestBuilder {
        private @NotNull(message = "Name should not be null")
        @NotBlank(message = "Name should not be blank")
        @NotEmpty(message = "Name should not be empty") String name;
        private @Email(message = "Email should have valid format")
        @NotNull(message = "Email should not be null")
        @NotBlank(message = "Email should not be blank")
        @NotEmpty(message = "Email should not be empty") String email;
        private String password;
        private @NotNull(message = "Favourite team should not be null")
        @NotBlank(message = "Favourite team should not be blank")
        @NotEmpty(message = "Favourite team should not be empty") String favouriteTeam;

        UserUpdateRequestBuilder() {
        }

        public UserUpdateRequestBuilder name(@NotNull(message = "Name should not be null") @NotBlank(message = "Name should not be blank") @NotEmpty(message = "Name should not be empty") String name) {
            this.name = name;
            return this;
        }

        public UserUpdateRequestBuilder email(@Email(message = "Email should have valid format") @NotNull(message = "Email should not be null") @NotBlank(message = "Email should not be blank") @NotEmpty(message = "Email should not be empty") String email) {
            this.email = email;
            return this;
        }

        public UserUpdateRequestBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserUpdateRequestBuilder favouriteTeam(@NotNull(message = "Favourite team should not be null") @NotBlank(message = "Favourite team should not be blank") @NotEmpty(message = "Favourite team should not be empty") String favouriteTeam) {
            this.favouriteTeam = favouriteTeam;
            return this;
        }

        public UserUpdateRequest build() {
            return new UserUpdateRequest(this.name, this.email, this.password, this.favouriteTeam);
        }

        public String toString() {
            return "UserUpdateRequest.UserUpdateRequestBuilder(name=" + this.name + ", email=" + this.email + ", password=" + this.password + ", favouriteTeam=" + this.favouriteTeam + ")";
        }
    }
}
