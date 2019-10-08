package com.lg.service;

import com.lg.entity.PageResult;
import com.lg.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
    /**
     * 新建预约数据
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    void deleteById(Integer id);

    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);
}
