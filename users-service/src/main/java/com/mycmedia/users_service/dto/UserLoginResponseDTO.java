package com.mycmedia.users_service.dto;

public class UserLoginResponseDTO {

    private String token;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String message;

    public UserLoginResponseDTO(String message) {
        this.message = message;
    }
}
