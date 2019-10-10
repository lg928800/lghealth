package com.lg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lg.Utils.QiniuUtils;
import com.lg.constant.MessageConstant;
import com.lg.constant.RedisConstant;
import com.lg.entity.PageResult;
import com.lg.entity.QueryPageBean;
import com.lg.entity.Result;
import com.lg.pojo.CheckGroup;
import com.lg.pojo.Setmeal;
import com.lg.service.CheckGroupService;
import com.lg.service.SetMealSerivce;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

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
    //注入jedis的属性
    @Autowired
    private JedisPool jedisPool;

    /**
     * 上传七牛云图片
     * @param imagefile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imagefile) {
        //获取原始的文件名-重命名文件-放置重名覆盖
        String filename = imagefile.getOriginalFilename();
        //在字符串中查找指定的索引
        int index = filename.lastIndexOf(".");
        //获取拓展名
        String ext = filename.substring(index);
        //生成自定义的文件名
        String finalFilename = UUID.randomUUID().toString()+ext;
        //文件上传-通过七牛云
        try {
            QiniuUtils.upload2Qiniu(imagefile.getBytes(),finalFilename);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_QNY_RESOURCES,finalFilename);
        } catch (IOException e) {
            LOGGER.error("Upload img error",e);
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
            return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,finalFilename);
    }

    /**
     * 添加套餐和检查组信息
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, @RequestParam Integer[] checkgroupIds) {
        try {
            setMealSerivce.add(setmeal,checkgroupIds);
            //添加成功后返回提示信息
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            LOGGER.error("Add setmeal error",e);
        }
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {

        return setMealSerivce.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());


    }
}
