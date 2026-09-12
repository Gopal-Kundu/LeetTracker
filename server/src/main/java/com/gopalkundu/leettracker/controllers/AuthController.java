package com.gopalkundu.leettracker.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gopalkundu.leettracker.entity.User;
import com.gopalkundu.leettracker.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired UserService userService;

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
}
