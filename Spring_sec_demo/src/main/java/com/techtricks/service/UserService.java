package com.techtricks.service;


import com.techtricks.model.Users;
import com.techtricks.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<Users>  getAllUsers(){
        return userRepo.findAll();
    }
}
