package org.example.userservice.dto;

public class LoginResponse {
    private String token;

    public LoginResponse() {
        // Optional, but recommended
    }

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
