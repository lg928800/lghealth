package com.lg.jobs;

import com.lg.Utils.QiniuUtils;
import com.lg.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import java.util.Iterator;
import java.util.Set;

/**
 * @version V1.0
 * @author: lg9288
 * @date: 2019/10/10 19:03
 * @description:
 */
public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;

    public void clearImg() {
        //比较上传图片的差值
        Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_QNY_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        Iterator<String> iter = sdiff.iterator();

        while (iter.hasNext()){
            //需要清理的图片的名字
            String filename = iter.next();

            System.out.println("=========>"+filename);

            //调用七牛云的sdk删除 图片
            QiniuUtils.deleteFileFromQiniu(filename);

            //删除保存在RedisConstant.SETMEAL_IMG_TMP 的图片名 filename
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_QNY_RESOURCES,filename);
        }
    }
}
