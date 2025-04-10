package com.lussomovano.backend.service;

import com.lussomovano.backend.entity.User;
import com.lussomovano.backend.repository.UserRepository;
import com.lussomovano.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList("USER"));

        return userRepository.save(user);
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(user.getEmail());
    }

//    public User login(String email, String rawPassword) {
////        User user = userRepository.findByEmail(email)
////                .orElseThrow(() -> new RuntimeException("User not found"));
////
////        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
////            throw new RuntimeException("Invalid credentials");
////        }
////        return user;
////    }
}
