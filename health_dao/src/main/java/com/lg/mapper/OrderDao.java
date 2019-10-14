package com.lg.mapper;

import com.lg.pojo.Order;

import java.util.Map;

public interface OrderDao {
    Order findByCondition(Order order);

    void add(Order addOrder);

    Map findById(Integer id);
}
