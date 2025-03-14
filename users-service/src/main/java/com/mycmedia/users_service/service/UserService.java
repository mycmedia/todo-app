package com.mycmedia.users_service.service;

import com.mycmedia.users_service.dto.UserRequestDTO;
import com.mycmedia.users_service.dto.UserResponseDTO;
import com.mycmedia.users_service.model.User;
import com.mycmedia.users_service.repository.UserRepository;
import com.mycmedia.users_service.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {

        User user = userMapper.toUserEntity(userRequestDTO);
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));  // Encrypt password
        user.setActive(true);
        user.setLocked(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return userMapper.userResponseDTO(user);
    }

    public UserResponseDTO getUser(Long id) {
        Optional<User> userOpt = userRepository.findByIdAndIsActiveTrue(id);
        if (userOpt.isPresent()) {
            return userMapper.userResponseDTO(userOpt.get());
        }
        throw new RuntimeException("User not found or inactive.");
    }

    public boolean authenticateUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return passwordEncoder.matches(password, user.getPassword());
        }

        return false;
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        Optional<User> userOpt = userRepository.findByIdAndIsActiveTrue(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setUsername(userRequestDTO.getUsername());
            user.setEmail(userRequestDTO.getEmail());
            user.setPhone(userRequestDTO.getPhone());
            user.setRole(userRequestDTO.getRole());
            user.setFirstName(userRequestDTO.getFirstName());
            user.setLastName(userRequestDTO.getLastName());
            user.setDateOfBirth(userRequestDTO.getDateOfBirth());
            user.setUpdatedAt(LocalDateTime.now());

            userRepository.save(user);
            return userMapper.userResponseDTO(user);
        }
        throw new RuntimeException("User not found or inactive.");
    }

    public void deleteUser(Long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userOpt = userRepository.findByIdAndIsActiveTrue(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getId().equals(currentUser.getId()) || currentUser.getRole().equals("ADMIN")) {
                userRepository.deleteById(id);
            } else {
                throw new RuntimeException("Permission denied.");
            }
        } else {
            throw new RuntimeException("User not found or inactive.");
        }
    }

    public void lockUser(Long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!currentUser.getRole().equals("ADMIN")) {
            throw new RuntimeException("Only admin can lock a user account.");
        }

        Optional<User> userOpt = userRepository.findByIdAndIsActiveTrue(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setLocked(true);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found or inactive.");
        }
    }

    public void unlockUser(Long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!currentUser.getRole().equals("ADMIN")) {
            throw new RuntimeException("Only admin can unlock a user account.");
        }

        Optional<User> userOpt = userRepository.findByIdAndIsActiveTrue(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setLocked(false);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found or inactive.");
        }
    }


    public String authenticateAndGenerateToken(String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        // Generate JWT Token
        return jwtTokenUtil.generateToken(username);
    }

}
