package com.lg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.lg.Utils.SMSUtils;
import com.lg.Utils.ValidateCodeUtils;
import com.lg.constant.MessageConstant;
import com.lg.constant.RedisConstant;
import com.lg.constant.RedisMessageConstant;
import com.lg.entity.Result;
import com.lg.pojo.Setmeal;
import com.lg.service.SetMealSerivce;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * @version V1.0
 * @author: lg9288
 * @date: 2019/10/7 15:13
 * @description:
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    //打印日志信息
    private static final Logger LOGGER = Logger.getLogger(ValidateCodeController.class);
    //创建service层业务接口

    //jedisPool
    @Autowired
    private JedisPool jedisPool;

    /**
     * 发送验证码
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Order")
    public Result send4Order(@RequestParam String telephone) {
        //获取前台的参数电话号码，从redis中查找是否存在其验证码
        String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //判断获取的验证码是否存在
        if (code != null) {
            return new Result(false,"验证码已发送，请注意查收！");
        }
        //如果不存在，则生成新的验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        //发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
            //将验证码保存到redis中
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 60 * 5, validateCode.toString());
        } catch (Exception e) {
            LOGGER.error("Sms send error",e);
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
