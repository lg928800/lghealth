package com.lg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lg.Utils.POIUtils;
import com.lg.Utils.QiniuUtils;
import com.lg.constant.MessageConstant;
import com.lg.constant.RedisConstant;
import com.lg.entity.PageResult;
import com.lg.entity.QueryPageBean;
import com.lg.entity.Result;
import com.lg.pojo.OrderSetting;
import com.lg.pojo.Setmeal;
import com.lg.service.OrderSettingService;
import com.lg.service.SetMealSerivce;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.*;

/**
 * @version V1.0
 * @author: lg9288
 * @date: 2019/10/7 15:13
 * @description:
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    //打印日志信息
    private static final Logger LOGGER = Logger.getLogger(OrderSettingController.class);
    //创建service层业务接口
    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 批量上传
     * @param file
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile file) {
        //读取上传的文件
        try {
            List<String[]> strings = POIUtils.readExcel(file);
            //判空
            if (strings != null) {
                //创建list集合用来封装ordersetting数据
                List<OrderSetting> list = new ArrayList<>();
                //遍历读取数据的集合数组
                for (String[] string : strings) {
                    //创建orderSetting对象
                    OrderSetting orderSetting = new OrderSetting(new Date(string[0]), Integer.parseInt(string[1]));
                    list.add(orderSetting);
                }
                //serivce层处理数据
                orderSettingService.add(list);
            }
            return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            //日志打印
            LOGGER.error("Add orderSetting error",e);
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /**
     * 查询预约的日期数据回显到页面上
     * @param date
     * @return
     */
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date) {
        //将返回结果封装到map泛型的集合中
        try {
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            LOGGER.error("Get ordersetting error",e);
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    /**
     * 修改预约可预约人数
     * @param orderSetting
     * @return
     */
    @RequestMapping("/settingByDay")
    public Result settingByDay(@RequestBody OrderSetting orderSetting) {
        try {
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            //打印日志
            LOGGER.error("Edit orderSetting error",e);
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
