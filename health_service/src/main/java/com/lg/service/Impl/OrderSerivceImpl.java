package com.lg.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lg.Utils.DateUtils;
import com.lg.constant.MessageConstant;
import com.lg.constant.RedisConstant;
import com.lg.entity.PageResult;
import com.lg.mapper.MemberDao;
import com.lg.mapper.OrderDao;
import com.lg.mapper.OrderSettingDao;
import com.lg.mapper.SetMealDao;
import com.lg.pojo.Member;
import com.lg.pojo.Order;
import com.lg.pojo.OrderSetting;
import com.lg.pojo.Setmeal;
import com.lg.service.OrderService;
import com.lg.service.SetMealSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: lg9288
 * @date: 2019/10/10 16:14
 * @description:
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderSerivceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;

    /**
     * 预约信息的提交
     *
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public Order add(Map map) throws Exception {
        //获取提交数据中的日期，查询该日期日否可预约
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        if (orderSetting == null) {
            throw new RuntimeException(MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //获取可预约人数
        int number = orderSetting.getNumber();
        //获取已预约人数
        int reservations = orderSetting.getReservations();
        //判断两者
        if (number < reservations || number == reservations) {
            throw new RuntimeException(MessageConstant.ORDER_FULL);
        }
        //获取电话号码
        String telephone = (String) map.get("telephone");
        //根据电话查询用户是否是会员
        Member member = memberDao.findByTelephone(telephone);
        //判断member是否为空，如果为空则添加会员信息
        if (member == null) {
            member = new Member();
            member.setName((String) map.get("name"));
            member.setSex((String) map.get("sex"));
            member.setIdCard((String) map.get("idCard"));
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            //添加新会员
            memberDao.add(member);
        } else {
            //根据日期判断日否已经预约
            Order order = new Order();
            order.setMemberId(member.getId());
            order.setOrderDate(date);
            order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));
            Order queryOrder = orderDao.findByCondition(order);
            //判空
            if (queryOrder != null) {
                //如果不为空则是已经预约了当日的体检
                throw new RuntimeException(MessageConstant.HAS_ORDERED);
            }
            //更新数据
            orderSetting.setReservations(orderSetting.getReservations() + 1);
            //修改数据库数据
            orderSettingDao.updateReservationsByDate(orderSetting);
        }
        //如果没有预约则添加预约
        Order addOrder = new Order();
        addOrder.setMemberId(member.getId());
        addOrder.setOrderDate(date);
        addOrder.setOrderType((String) map.get("orderType"));
        addOrder.setOrderStatus(Order.ORDERSTATUS_NO);
        addOrder.setSetmealId(Integer.parseInt((String) map.get("setmealId")));
        orderDao.add(addOrder);
        return addOrder;
    }

    /**
     * 查询3表的信息回显到提交成功的页面上
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public Map findById(Integer id) throws Exception {
        Map map = orderDao.findById(id);
        if (map != null) {
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate", DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
