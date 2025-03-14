package com.mycmedia.users_service.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponseDTO {
    private String token;
    private String message;

    public UserLoginResponseDTO(String message) {
        this.message = message;
    }
}