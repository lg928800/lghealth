package com.lg.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lg.constant.RedisConstant;
import com.lg.entity.PageResult;
import com.lg.mapper.OrderSettingDao;
import com.lg.mapper.SetMealDao;
import com.lg.pojo.OrderSetting;
import com.lg.pojo.Setmeal;
import com.lg.service.OrderSettingService;
import com.lg.service.SetMealSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingSerivceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        //先查询是否又该日期
        long count = orderSettingDao.findCountByOrderdate(orderSetting.getOrderDate());
        //判断count是否大于0
        if (count > 0) {
            //如果存在则调用修改
            orderSettingDao.updateToOrderdate(orderSetting);
        } else {
            //否则添加新的数据
            orderSettingDao.add(orderSetting);
        }

    }

    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        //date中的值:2019-10
        //设置开始日期-加上日
        //开始日期
        String dateBegin = date+"-1";//2019-10-1
        //末尾日期
        String dateEnd = date+"-31";//2019-10-31
        Map<String,String> stringMap =new HashMap<>();
        stringMap.put("dateBegin",dateBegin);
        stringMap.put("dateEnd",dateEnd);
        //查询开始日期到末尾日期的预约数据
        List<OrderSetting> list = orderSettingDao.queryBeginAndLastDate(stringMap);
        //创建泛型为map的list集合
        List<Map> data = new ArrayList<>();
        //遍历查询结果
        for (OrderSetting orderSetting : list) {
                //创建map集合封装数据
            Map orderSettingMap = new HashMap<>();
            orderSettingMap.put("date",orderSetting.getOrderDate().getDate());
            orderSettingMap.put("number",orderSetting.getNumber());
            orderSettingMap.put("reservations",orderSetting.getReservations());
            data.add(orderSettingMap);
        }


        return data;

    }

    /**
     * 批量导入数据
      * @param list
     */
    @Override
    public void add(List<OrderSetting> list) {
        //批量导入数据，先判断参数是否存在
        if (list != null && list.size() > 0) {
            //判空后，遍历集合
            for (OrderSetting orderSetting : list) {
                //1.先查询数据库中是否存在相同日期
                long count = orderSettingDao.findCountByOrderdate(orderSetting.getOrderDate());
                //2.判断count是否大于0，及存在相同日期，则修改数据
                if (count > 0) {
                    //修改日期数据
                    orderSettingDao.updateToOrderdate(orderSetting);
                } else {
                    //否则，添加数据
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }
}
