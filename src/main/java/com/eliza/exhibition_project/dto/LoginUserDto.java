package com.eliza.exhibition_project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class LoginUserDto {

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "The email must be a valid email address")
    private String email;

    @NotEmpty(message = "Password hash should not be empty")
    private String passwordHash;

    public  String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
