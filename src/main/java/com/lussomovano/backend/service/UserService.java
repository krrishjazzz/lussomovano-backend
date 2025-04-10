package com.lussomovano.backend.service;

import com.lussomovano.backend.entity.User;

public interface UserService {
    User findByEmail(String email);
    User save(User user);
}
