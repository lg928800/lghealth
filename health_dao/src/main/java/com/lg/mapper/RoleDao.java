package com.lg.mapper;

import com.lg.pojo.Role;

import java.util.Set;

public interface RoleDao {
    Set<Role> findById(Integer id);
}
