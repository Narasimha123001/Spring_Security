package com.techtricks.service;

import com.techtricks.model.UserPrincipal;
import com.techtricks.model.Users;
import com.techtricks.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {


    private final UserRepo userRepo;

    public MyUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    //here its checks the user is present in the database or not
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByUsername(username);
        if (user == null) {  // if the user is not found
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("user not found");
        }
        // if the user is found then it will return the userPrincipal
        return new UserPrincipal(user);
    }

    // here a thread will come from the controller to save the user
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);  // create a obj for encoding
    public Users register(Users user) {
        user.setPassword(encoder.encode(user.getPassword())); // hashed the password
        userRepo.save(user);  //save the user while using the save method from the repo
        return user;
    }


    public List<Users> getAllUsers(){
        return userRepo.findAll();
    }
}
