package com.lg.mapper;

import com.lg.pojo.Permission;

import java.util.Set;

public interface PermissionDao {
    Set<Permission> findById(Integer id);
}
