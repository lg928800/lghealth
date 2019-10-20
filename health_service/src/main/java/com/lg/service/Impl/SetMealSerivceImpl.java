package com.lg.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lg.constant.RedisConstant;
import com.lg.entity.PageResult;
import com.lg.mapper.OrderDao;
import com.lg.mapper.SetMealDao;
import com.lg.pojo.Setmeal;
import com.lg.service.SetMealSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: lg9288
 * @date: 2019/10/10 16:14
 * @description:
 */
@Service(interfaceClass = SetMealSerivce.class)
@Transactional
public class SetMealSerivceImpl implements SetMealSerivce {
    //创建dao层
    @Autowired
    private SetMealDao setMealDao;
    @Autowired
    private JedisPool jedisPool;

    /**
     * 第三次分页查询，套餐
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //使用框架pagehelper
        PageHelper.startPage(currentPage,pageSize);
        //dao层查询数据.返回值为PAGE
        Page<Setmeal> page = setMealDao.findPage(queryString);

        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 添加检查组和套餐的信息
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //1.先添加setmeal套餐的信息
        setMealDao.add(setmeal);
        //建立套餐表和检查组关联的关系
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            setSetMealAndCheckGroupIds(setmeal.getId(),checkgroupIds);
        }
        //将图片保存到redis中
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
    }

    private void setSetMealAndCheckGroupIds(Integer setmealid, Integer[] checkgroupIds) {
        //判断Ids的数据，是否存在
        if (checkgroupIds == null && checkgroupIds.length == 0) {
            //如果不存在则直接结束此方法
            return;
        } else {
            //如果存在则遍历数组
            for (Integer checkgroupId : checkgroupIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("setmeal_id",setmealid);
                map.put("checkgroup_id",checkgroupId);
                setMealDao.setSetMealAndCheckGroupIds(map);
            }
        }
    }


    /**
     * 通过ID查询套餐及检查表和检查组的信息
     * @param id
     * @return
     */
    @Override
    public String findById(Integer id) {
        //从redis中获取k值
        Jedis jedis = jedisPool.getResource();
        String dataId = jedis.get("SetMeal_Data_Id");
        //判断是否存在数据，如果不存在则从数据库中查询，然后添加到redis中
        if (dataId == null) {
            Setmeal setmeal = setMealDao.findById(id);
            if (setmeal != null) {
                //将数据库中的数据添加到redis中
                jedis.set("SetMeal_Data_Id",setmeal.toString());
            }
        }
        return dataId;
    }

    /**
     * 查询套餐所有信息
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
        return setMealDao.findAll();
    }
}
