package com.matchmetrics.security.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class AuthenticationRequest {
    @Email(message = "Email should have valid format")
    @NotNull(message = "Email should not be null")
    @NotBlank(message = "Email should not be blank")
    @NotEmpty(message = "Email should not be empty")
    private String email;

    @NotNull(message = "Password should not be null")
    @NotBlank(message = "Password should not be blank")
    @NotEmpty(message = "Password should not be empty")
    private String password;

    public AuthenticationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public AuthenticationRequest() {
    }

    public static AuthenticationRequestBuilder builder() {
        return new AuthenticationRequestBuilder();
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof AuthenticationRequest)) return false;
        final AuthenticationRequest other = (AuthenticationRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$email = this.getEmail();
        final Object other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) return false;
        final Object this$password = this.getPassword();
        final Object other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof AuthenticationRequest;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $email = this.getEmail();
        result = result * PRIME + ($email == null ? 43 : $email.hashCode());
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        return result;
    }

    public String toString() {
        return "AuthenticationRequest(email=" + this.getEmail() + ", password=" + this.getPassword() + ")";
    }

    public static class AuthenticationRequestBuilder {
        private String email;
        private String password;

        AuthenticationRequestBuilder() {
        }

        public AuthenticationRequestBuilder email(String email) {
            this.email = email;
            return this;
        }

        public AuthenticationRequestBuilder password(String password) {
            this.password = password;
            return this;
        }

        public AuthenticationRequest build() {
            return new AuthenticationRequest(this.email, this.password);
        }

        public String toString() {
            return "AuthenticationRequest.AuthenticationRequestBuilder(email=" + this.email + ", password=" + this.password + ")";
        }
    }
}
