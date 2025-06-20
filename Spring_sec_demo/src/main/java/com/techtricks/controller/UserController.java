package com.techtricks.controller;

import com.techtricks.model.Users;
import com.techtricks.service.MyUserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {


    private final  MyUserDetailsService service;

    public UserController(MyUserDetailsService service) {
        this.service = service;
    }


    //used to register and send to my userDetailsService to save the user
    @PostMapping("/register")
    public Users register(@RequestBody Users user) {
        return service.register(user);
    }


    @GetMapping("/users")
    public List<Users> getUsers() {
        return service.getAllUsers();
    }
}
