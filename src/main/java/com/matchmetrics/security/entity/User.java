package com.matchmetrics.security.entity;

import com.matchmetrics.entity.Team;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
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
}
