package com.lg.service;

import com.lg.pojo.User;

public interface UserService {
    User findByUsername(String username);
}
