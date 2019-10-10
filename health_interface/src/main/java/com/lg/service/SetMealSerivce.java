package com.lg.service;

import com.lg.entity.PageResult;
import com.lg.pojo.Setmeal;

public interface SetMealSerivce {
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);
}
