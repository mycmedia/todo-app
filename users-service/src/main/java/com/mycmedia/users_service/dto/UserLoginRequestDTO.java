package com.mycmedia.users_service.dto;

import lombok.Data;

@Data
public class UserLoginRequestDTO {
    private String username;
    private String password;
}