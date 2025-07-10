package com.api.testing.models.login;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {
    private String token;

    // Default constructor
    public LoginResponse() {}

    public LoginResponse(String token) {
        this.token = token;
    }

    // Getter and setter

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
