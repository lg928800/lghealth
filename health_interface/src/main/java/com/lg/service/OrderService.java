package com.lg.service;

import com.lg.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Order add(Map map) throws Exception;

    Map findById(Integer id) throws Exception;

    List<Map<String, Object>> getSetmealReport();
}
