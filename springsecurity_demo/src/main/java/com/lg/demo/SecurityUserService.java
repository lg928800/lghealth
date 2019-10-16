package com.lg.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: lg9288
 * @date: 2019/10/16 16:46
 * @description:
 */
@Service
public class SecurityUserService implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    //定义map集合
    private Map map = new HashMap();

    public void initData() {
        User user1 = new User();
        user1.setUsername("zhangsan");
        user1.setPassword(bCryptPasswordEncoder.encode("123456"));
        //定义第二个user
        User user2 = new User();
        user2.setUsername("lisi");
        user2.setPassword(bCryptPasswordEncoder.encode("123456"));

        map.put(user1.getUsername(),user1);
        map.put(user2.getUsername(),user2);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //加载初始方法
        initData();
        //获取用户输入的用户名username
        User user = (User) map.get(username);

        //创建list集合封装授权信息
        List<GrantedAuthority> list = new ArrayList<>();
        //添加授权的规则
        list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        list.add(new SimpleGrantedAuthority("add"));
        //设置明文密码
        //String password = "{noop}"+user.getPassword();
        //封装到security框架提供的user中
        org.springframework.security.core.userdetails.User detailUser = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), list);

        return detailUser;
    }
}
