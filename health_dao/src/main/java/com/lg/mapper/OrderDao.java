package com.lg.mapper;

import com.lg.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    Order findByCondition(Order order);

    void add(Order addOrder);

    Map findById(Integer id);

    List<Map<String, Object>> getSetmealReport();

    Integer findTodayOrderNumber(@Param("today") String today);

    Integer findOrderNumberAfterDate(@Param("date") String date);

    Integer findTodayVisitsNumber(@Param("today")String today);

    Integer findVisitsNumberAfterDate(@Param("date")String date);

    List<Map> findHotSetmeal();
}
