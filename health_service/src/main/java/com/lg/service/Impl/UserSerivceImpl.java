package com.lg.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lg.constant.RedisConstant;
import com.lg.entity.PageResult;
import com.lg.mapper.PermissionDao;
import com.lg.mapper.RoleDao;
import com.lg.mapper.SetMealDao;
import com.lg.mapper.UserDao;
import com.lg.pojo.Permission;
import com.lg.pojo.Role;
import com.lg.pojo.Setmeal;
import com.lg.pojo.User;
import com.lg.service.SetMealSerivce;
import com.lg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @version V1.0
 * @author: lg9288
 * @date: 2019/10/10 16:14
 * @description:
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserSerivceImpl implements UserService {
    //定义dao层对象
    @Autowired
    private UserDao userDao;
    //定义roleDao层对象
    @Autowired
    private RoleDao roleDao;
    //定义permissionDao层对象
    @Autowired
    private PermissionDao permissionDao;

    /**
     * 多表查询，封装权限和角色的数据
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);
        //通过roleDao根据userId来查询role
        Set<Role> roles = roleDao.findById(user.getId());
        //遍历roles
        if (roles != null && roles.size() > 0) {
            for (Role role : roles) {
                //根据role角色id来查询permissions权限
                Set<Permission> permissions = permissionDao.findById(role.getId());
                if (permissions != null && permissions.size() > 0) {
                    //将数据添加到role中
                    role.setPermissions(permissions);
                }
            }
            user.setRoles(roles);
        }
        return user;
    }
}
