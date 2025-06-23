package com.techtricks.controller;


import com.techtricks.model.Users;
import com.techtricks.repo.UserRepo;
import com.techtricks.service.UserService;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    private final UserService userService;

    public TestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<Users> getAllUser(){
        return userService.getAllUsers();
    }
}
