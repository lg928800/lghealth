package com.lg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lg.constant.MessageConstant;
import com.lg.constant.RedisMessageConstant;
import com.lg.entity.Result;
import com.lg.service.MemberService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @version V1.0
 * @author: lg9288
 * @date: 2019/10/7 15:13
 * @description:
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    //打印日志信息
    private static final Logger LOGGER = Logger.getLogger(LoginController.class);
    //创建service层业务接口
    @Reference
    private MemberService memberSerivce;
    //jedisPool
    @Autowired
    private JedisPool jedisPool;

    /**
     * 用户登录业务
     * @param response
     * @param map
     * @return
     */
    @RequestMapping("/check")
    public Result check(HttpServletResponse response, @RequestBody Map map) {
        try {
            //同上次一样,从提交过来的参数中获取电话信息和验证码
            String telephone = (String) map.get("telephone");
            //验证码
            String validateCode = (String) map.get("validateCode");
            //从redis中获取之前保存的验证码，判断是否与请求过来的一样
            String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
            if (code == null || !(code.equals(validateCode))) {
                return new Result(false,MessageConstant.VALIDATECODE_ERROR);
            }
            //调用serivce层处理数据
            memberSerivce.check(map);
            //将用户输入的电话号码保存到cookie中
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            //设置路径
            cookie.setPath("/");
            //设置保存时间
            cookie.setMaxAge(60*60*24*30);
            response.addCookie(cookie);
        } catch (Exception e) {
            LOGGER.error("Login error",e);
        }
        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }

}
