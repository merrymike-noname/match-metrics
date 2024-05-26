package com.matchmetrics.security.entity;

import com.matchmetrics.entity.Team;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @NotNull(message = "Name should not be null")
    @NotBlank(message = "Name should not be blank")
    @NotEmpty(message = "Name should not be empty")
    private String name;

    @Column(name = "email", unique = true)
    @Email(message = "Email should have valid format")
    @NotNull(message = "Email should not be null")
    @NotBlank(message = "Email should not be blank")
    @NotEmpty(message = "Email should not be empty")
    private String email;

    @Column(name = "password")
    @NotNull(message = "Password should not be null")
    @NotBlank(message = "Password should not be blank")
    @NotEmpty(message = "Password should not be empty")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "favourite_team_id", referencedColumnName = "id")
    private Team favouriteTeam;

    public User(int id, @NotNull(message = "Name should not be null") @NotBlank(message = "Name should not be blank") @NotEmpty(message = "Name should not be empty") String name, @Email(message = "Email should have valid format") @NotNull(message = "Email should not be null") @NotBlank(message = "Email should not be blank") @NotEmpty(message = "Email should not be empty") String email, @NotNull(message = "Password should not be null") @NotBlank(message = "Password should not be blank") @NotEmpty(message = "Password should not be empty") String password, Role role, Team favouriteTeam) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.favouriteTeam = favouriteTeam;
    }

    public User() {
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(name, user.name)
                && Objects.equals(email, user.email)
                && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public int getId() {
        return this.id;
    }

    public @NotNull(message = "Name should not be null") @NotBlank(message = "Name should not be blank") @NotEmpty(message = "Name should not be empty") String getName() {
        return this.name;
    }

    public @Email(message = "Email should have valid format") @NotNull(message = "Email should not be null") @NotBlank(message = "Email should not be blank") @NotEmpty(message = "Email should not be empty") String getEmail() {
        return this.email;
    }

    public Role getRole() {
        return this.role;
    }

    public Team getFavouriteTeam() {
        return this.favouriteTeam;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(@NotNull(message = "Name should not be null") @NotBlank(message = "Name should not be blank") @NotEmpty(message = "Name should not be empty") String name) {
        this.name = name;
    }

    public void setEmail(@Email(message = "Email should have valid format") @NotNull(message = "Email should not be null") @NotBlank(message = "Email should not be blank") @NotEmpty(message = "Email should not be empty") String email) {
        this.email = email;
    }

    public void setPassword(@NotNull(message = "Password should not be null") @NotBlank(message = "Password should not be blank") @NotEmpty(message = "Password should not be empty") String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setFavouriteTeam(Team favouriteTeam) {
        this.favouriteTeam = favouriteTeam;
    }

    public String toString() {
        return "User(id=" + this.getId() + ", name=" + this.getName() + ", email=" + this.getEmail() + ", password=" + this.getPassword() + ", role=" + this.getRole() + ", favouriteTeam=" + this.getFavouriteTeam() + ")";
    }

    public static class UserBuilder {
        private int id;
        private @NotNull(message = "Name should not be null")
        @NotBlank(message = "Name should not be blank")
        @NotEmpty(message = "Name should not be empty") String name;
        private @Email(message = "Email should have valid format")
        @NotNull(message = "Email should not be null")
        @NotBlank(message = "Email should not be blank")
        @NotEmpty(message = "Email should not be empty") String email;
        private @NotNull(message = "Password should not be null")
        @NotBlank(message = "Password should not be blank")
        @NotEmpty(message = "Password should not be empty") String password;
        private Role role;
        private Team favouriteTeam;

        UserBuilder() {
        }

        public UserBuilder id(int id) {
            this.id = id;
            return this;
        }

        public UserBuilder name(@NotNull(message = "Name should not be null") @NotBlank(message = "Name should not be blank") @NotEmpty(message = "Name should not be empty") String name) {
            this.name = name;
            return this;
        }

        public UserBuilder email(@Email(message = "Email should have valid format") @NotNull(message = "Email should not be null") @NotBlank(message = "Email should not be blank") @NotEmpty(message = "Email should not be empty") String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(@NotNull(message = "Password should not be null") @NotBlank(message = "Password should not be blank") @NotEmpty(message = "Password should not be empty") String password) {
            this.password = password;
            return this;
        }

        public UserBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public UserBuilder favouriteTeam(Team favouriteTeam) {
            this.favouriteTeam = favouriteTeam;
            return this;
        }

        public User build() {
            return new User(this.id, this.name, this.email, this.password, this.role, this.favouriteTeam);
        }

        public String toString() {
            return "User.UserBuilder(id=" + this.id + ", name=" + this.name + ", email=" + this.email + ", password=" + this.password + ", role=" + this.role + ", favouriteTeam=" + this.favouriteTeam + ")";
        }
    }
}
