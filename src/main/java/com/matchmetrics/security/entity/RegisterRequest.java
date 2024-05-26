package com.matchmetrics.security.entity;

public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private int favouriteTeamId;

    public RegisterRequest(String name, String email, String password, int favouriteTeamId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.favouriteTeamId = favouriteTeamId;
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

    public int getFavouriteTeamId() {
        return this.favouriteTeamId;
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

    public void setFavouriteTeamId(int favouriteTeamId) {
        this.favouriteTeamId = favouriteTeamId;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof RegisterRequest)) return false;
        final RegisterRequest other = (RegisterRequest) o;
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
        if (this.getFavouriteTeamId() != other.getFavouriteTeamId()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof RegisterRequest;
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
        result = result * PRIME + this.getFavouriteTeamId();
        return result;
    }

    public String toString() {
        return "RegisterRequest(name=" + this.getName() + ", email=" + this.getEmail() + ", password=" + this.getPassword() + ", favouriteTeamId=" + this.getFavouriteTeamId() + ")";
    }

    public static class RegisterRequestBuilder {
        private String name;
        private String email;
        private String password;
        private int favouriteTeamId;

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

        public RegisterRequestBuilder favouriteTeamId(int favouriteTeamId) {
            this.favouriteTeamId = favouriteTeamId;
            return this;
        }

        public RegisterRequest build() {
            return new RegisterRequest(this.name, this.email, this.password, this.favouriteTeamId);
        }

        public String toString() {
            return "RegisterRequest.RegisterRequestBuilder(name=" + this.name + ", email=" + this.email + ", password=" + this.password + ", favouriteTeamId=" + this.favouriteTeamId + ")";
        }
    }
}
