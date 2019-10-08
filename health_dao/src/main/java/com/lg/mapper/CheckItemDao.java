package com.lg.mapper;

import com.github.pagehelper.Page;
import com.lg.entity.Result;
import com.lg.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    //新增数据，新建预约
    void add(CheckItem checkItem);
    //分页查询
    Page<CheckItem> selectByCondition(String queryString);

    void deleteById(Integer id);

    Long findCountByCheckItemId(Integer id);

    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);
}
