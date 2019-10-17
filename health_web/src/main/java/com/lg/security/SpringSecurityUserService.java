package com.lg.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lg.pojo.Permission;
import com.lg.pojo.Role;
import com.lg.pojo.User;
import com.lg.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @version V1.0
 * @author: lg9288
 * @date: 2019/10/16 20:14
 * @description:
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {

    //定义userService，方便查询数据
    @Reference
    private UserService userService;

    /**
     * 接受前台穿过来的username数据，并定义权限
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户传过来的username来查询user数据
        User user = userService.findByUsername(username);
        //定义list集合来封装权限
        Set<GrantedAuthority> list = new HashSet<>();

        //从uer中获取role角色数据
        Set<Role> roles = user.getRoles();
        //判空，然后遍历
        if (roles != null && roles.size() > 0) {
            for (Role role : roles) {
                //通过每个角色获取permissions的权限
                Set<Permission> permissions = role.getPermissions();
                //判空，并且遍历
                if (permissions != null && permissions.size() > 0) {
                    for (Permission permission : permissions) {
                        //将权限添加到granted的集合中
                        list.add(new SimpleGrantedAuthority(permission.getKeyword()));
                    }
                }
            }
        }

        //将数据封装到userDetails
        org.springframework.security.core.userdetails.User userdetails = new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
        return userdetails;
    }
}
