package com.lg.mapper;

import com.github.pagehelper.Page;
import com.lg.pojo.Setmeal;

import java.util.Map;

public interface SetMealDao {
    void add(Setmeal setmeal);

    void setSetMealAndCheckGroupIds(Map<String, Integer> map);

    Page<Setmeal> findPage(String queryString);
}
