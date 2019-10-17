package com.lg.mapper;

import com.lg.pojo.User;

public interface UserDao {
    User findByUsername(String username);
}
