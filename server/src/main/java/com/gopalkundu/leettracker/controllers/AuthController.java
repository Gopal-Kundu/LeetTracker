package com.gopalkundu.leettracker.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gopalkundu.leettracker.entity.User;
import com.gopalkundu.leettracker.services.JwtService;
import com.gopalkundu.leettracker.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired 
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody User user){
        Map<String, Object> map = new HashMap<>();

        if(user.getUsername() == null || user.getUsername().trim().isEmpty()
        || user.getPassword() == null || user.getPassword().trim().isEmpty()){
            map.put("success", false);
            map.put("message", "Invalid Credentials !");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }
        
        Optional<User> res = userService.createUser(user);
        if(res.isEmpty()){
            map.put("success", false);
            map.put("message", "User already exists or can't sign up!");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(map);
        }
        map.put("success", true);
        map.put("message", "User Created");
        return ResponseEntity.ok(map);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User user) {
        Map<String, Object> map = new HashMap<>();

        if (user.getUsername() == null || user.getUsername().trim().isEmpty()
                || user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            map.put("success", false);
            map.put("message", "Invalid Credentials !");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(user.getUsername());

                ResponseCookie cookie = ResponseCookie.from("token", token)
                        .httpOnly(true)
                        .secure(false) 
                        .path("/")
                        .maxAge(30L * 24 * 60 * 60) // 30 days
                        .sameSite("Lax")
                        .build();

                map.put("success", true);
                map.put("message", "Login successful");

                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, cookie.toString())
                        .body(map);
            } else {
                map.put("success", false);
                map.put("message", "Authentication failed");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
            }
        } catch (BadCredentialsException e) {
            map.put("success", false);
            map.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
        } catch (Exception e) {
            map.put("success", false);
            map.put("message", "Authentication error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }
}

