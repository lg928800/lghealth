package com.lg.controller;

import com.lg.constant.MessageConstant;
import com.lg.entity.Result;
import com.lg.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version V1.0
 * @author: lg9288
 * @date: 2019/10/7 15:13
 * @description:
 */
@RestController
@RequestMapping("/user")
public class UserController {
    //打印日志信息
    private static final Logger LOGGER = Logger.getLogger(UserController.class);

    /**
     * 从框架中获取用户名称并回显到页面
     * @return
     */
    @RequestMapping("/getUsername")
    public Result getUsername() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user != null&&user.getUsername()!=null) {
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        }
        return new Result(false,MessageConstant.GET_USERNAME_FAIL);
    }
}
