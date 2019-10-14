package com.lg.mapper;

import com.lg.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    long findCountByOrderdate(Date orderDate);

    void updateToOrderdate(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);

    List<OrderSetting> queryBeginAndLastDate(Map date);

    OrderSetting findByOrderDate(Date date);

    void updateReservationsByDate(OrderSetting orderSetting);
}
