package com.techtricks.controller;

import com.techtricks.config.SecurityConfig;
import com.techtricks.model.Users;
import com.techtricks.service.JwtService;
import com.techtricks.service.MyUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {


    private final  MyUserDetailsService service;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public UserController(MyUserDetailsService service, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.service = service;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    //used to register and send to my userDetailsService to save the user
    @PostMapping("/register")
    public Users register(@RequestBody Users user) {
        return service.register(user);
    }


    @PostMapping("/login")
    public String login(@RequestBody Users user) {

        Authentication authentication =authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(user.getUsername());
        }
        else{
            return "failure";
        }
    }
}
