package com.gopalkundu.leettracker.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gopalkundu.leettracker.entity.User;
import com.gopalkundu.leettracker.repositories.UserRepository;

@Service
public class UserService {
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    public Optional<User> createUser(User user) {
        if ((user.get_id() != null && userRepository.findById(user.get_id()).isPresent())
                || userRepository.findByUsername(user.getUsername()).isPresent()) {
            return Optional.empty();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return Optional.of(userRepository.save(user));
    }
}
