package com.mycmedia.users_service.service;

import com.mycmedia.users_service.dto.UserRequestDTO;
import com.mycmedia.users_service.dto.UserResponseDTO;
import com.mycmedia.users_service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO userResponseDTO(User user){
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setUsername(user.getUsername());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setPhone(user.getPhone());
        responseDTO.setRole(user.getRole());
        responseDTO.setIsActive(user.isActive());
        responseDTO.setIsLocked(user.isLocked());
        responseDTO.setFirstName(user.getFirstName());
        responseDTO.setLastName(user.getLastName());
        responseDTO.setDateOfBirth(user.getDateOfBirth());
        responseDTO.setCreatedAt(user.getCreatedAt());
        responseDTO.setUpdatedAt(user.getUpdatedAt());
        responseDTO.setLastLogin(user.getLastLogin());
        return responseDTO;
    }


    public User toUserEntity(UserRequestDTO requestDTO) {
        User user = new User();
        user.setUsername(requestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setEmail(requestDTO.getEmail());
        user.setPhone(requestDTO.getPhone());
        user.setRole(requestDTO.getRole());
        user.setDateOfBirth(requestDTO.getDateOfBirth());
        user.setFirstName(requestDTO.getFirstName());
        user.setLastName(requestDTO.getLastName());
        return user;
    }
}
