package com.matchmetrics.security.entity;

public class AuthenticationResponse {
    private String token;

    public AuthenticationResponse(String token) {
        this.token = token;
    }

    public AuthenticationResponse() {
    }

    public static AuthenticationResponseBuilder builder() {
        return new AuthenticationResponseBuilder();
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof AuthenticationResponse)) return false;
        final AuthenticationResponse other = (AuthenticationResponse) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$token = this.getToken();
        final Object other$token = other.getToken();
        if (this$token == null ? other$token != null : !this$token.equals(other$token)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof AuthenticationResponse;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $token = this.getToken();
        result = result * PRIME + ($token == null ? 43 : $token.hashCode());
        return result;
    }

    public String toString() {
        return "AuthenticationResponse(token=" + this.getToken() + ")";
    }

    public static class AuthenticationResponseBuilder {
        private String token;

        AuthenticationResponseBuilder() {
        }

        public AuthenticationResponseBuilder token(String token) {
            this.token = token;
            return this;
        }

        public AuthenticationResponse build() {
            return new AuthenticationResponse(this.token);
        }

        public String toString() {
            return "AuthenticationResponse.AuthenticationResponseBuilder(token=" + this.token + ")";
        }
    }
}
