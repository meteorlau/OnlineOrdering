package org.example.userservice.dto;

public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest() {
        // Needed for JSON deserialization
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
