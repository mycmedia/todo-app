package com.mycmedia.users_service.controller;


import com.mycmedia.users_service.model.User;
import com.mycmedia.users_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String getUsers() {
        return "Hello World";
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/{username}")
    public User loginUser(@RequestBody User user) {
        return userService.findUserByUsername(user.getUsername());
    }

}
