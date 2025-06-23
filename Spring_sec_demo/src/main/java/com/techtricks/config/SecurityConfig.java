package com.techtricks.config;

import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {



    private final UserDetailsService userDetailsService;

    private final JwtFilter jwtFilter;

    public SecurityConfig(UserDetailsService userDetailsService, JwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    //clients send the basic auth details to the server and comes to the securityFilterChain
    // to checks the headers  (Authorization: Basic bmFyYXNpbWhhOnRlc3RwYXNzd29yZA==)

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.csrf(AbstractHttpConfigurer::disable).
                authorizeHttpRequests(request -> request
                        .requestMatchers("/register","login").permitAll()
                        .anyRequest().authenticated()).
//                httpBasic(Customizer.withDefaults()).
                sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class)
                .build();




    }


    //AuthenticationManager → AuthenticationProvider → UserDetailsService

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();  // used for loading user details and validating the provided the stored one
        provider.setUserDetailsService(userDetailsService);  //call the username from the database
        provider.setPasswordEncoder(passwordEncoder());
        // Encrypts incoming (raw) password and compares it with stored (hashed) password in DB using BCrypt
        return provider;
    }


    //here we want the manager to authenticate the user
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }



    // from the current(UserPrincipal)  here to get passwords and matches with BCryptPasswordEncoder.matches() → password check
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
