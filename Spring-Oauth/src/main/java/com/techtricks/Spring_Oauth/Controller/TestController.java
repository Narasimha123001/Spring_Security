package com.techtricks.Spring_Oauth.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/hello")
    public String getMessage(){

        return "Hello sir";
    }
}
