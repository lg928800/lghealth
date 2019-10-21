package com.lg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lg.constant.MessageConstant;
import com.lg.entity.Result;
import com.lg.pojo.Setmeal;
import com.lg.service.SetMealSerivce;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @version V1.0
 * @author: lg9288
 * @date: 2019/10/7 15:13
 * @description:
 */
@RestController
@RequestMapping("/setmeal")
public class SetMealController {
    //打印日志信息
    private static final Logger LOGGER = Logger.getLogger(SetMealController.class);
    //创建service层业务接口
    @Reference
    private SetMealSerivce setMealSerivce;

    /**
     * 查询套餐信息回显到前台
     * @return
     */
    @RequestMapping("/getSetmeal")
    public Result findAll() {
        try {
            String list = setMealSerivce.findAll();
            List<Setmeal> setmealList = JSON.parseArray(list, Setmeal.class);
            if (list != null) {
                return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmealList);
            }
        } catch (Exception e) {
            LOGGER.error("Find setMeal all error",e);
        }
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
    }

    /**
     * 通过ID查询套餐及检查组和检查项的信息
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    @ResponseBody
    public Result findById(@RequestParam("id") Integer id) {
        try {
            //将返回值改成string类型
            String data = setMealSerivce.findById(id);
            JSONObject setmeal = JSON.parseObject(data);
            if (setmeal != null) {

                return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
            }
        } catch (Exception e) {
            LOGGER.error("Find setMeal by id error",e);
        }
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
    }
}
