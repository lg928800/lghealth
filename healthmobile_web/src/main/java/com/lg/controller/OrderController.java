package com.lg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lg.Utils.SMSUtils;
import com.lg.Utils.ValidateCodeUtils;
import com.lg.constant.MessageConstant;
import com.lg.constant.RedisMessageConstant;
import com.lg.entity.Result;
import com.lg.pojo.Order;
import com.lg.service.OrderService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @version V1.0
 * @author: lg9288
 * @date: 2019/10/7 15:13
 * @description:
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    //打印日志信息
    private static final Logger LOGGER = Logger.getLogger(OrderController.class);
    //创建service层业务接口
    @Reference
    private OrderService orderService;
    //jedisPool
    @Autowired
    private JedisPool jedisPool;

    /**
     * 提交预约信息
     * @param map
     * @return
     */
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map) {
        //由于提交到后台的数据中有验证码及手机号等这些没有用Pojo来封装数据的参数，所以这里用map集合来接受参数
        //获取手机号
        try {
            String telephone = (String) map.get("telephone");
            //获取其中的验证码
            String validateCode = (String) map.get("validateCode");
            //从redis中获取之前发送的验证码
            String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
            //判断两个验证码是否相同
            if (code == null || !(code.equals(validateCode))) {
                return new Result(false,MessageConstant.VALIDATECODE_ERROR);
            }
            //设置预约类型
            map.put("orderType",Order.ORDERTYPE_WEIXIN);
            Order order = orderService.add(map);
            //判断预约是否成功
            if (order != null) {
                //预约成功发送短信通知
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,ValidateCodeUtils.generateValidateCode(4).toString());
                return new Result(true,MessageConstant.ORDERSETTING_SUCCESS,order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,MessageConstant.ORDERSETTING_FAIL);
    }

    /**
     * 根据id查询3表信息，并回显到页面
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(@RequestParam("id") Integer id) {
        //预约成功后回显多表的信息，用map接收，不建议
        try {
            Map map = orderService.findById(id);
            if (map != null) {
                return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
            }
        } catch (Exception e) {
            LOGGER.error("Find order by id error",e);
        }
        return new Result(false,MessageConstant.QUERY_ORDER_FAIL);

    }

}
