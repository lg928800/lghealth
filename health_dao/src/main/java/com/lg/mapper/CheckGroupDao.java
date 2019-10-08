package com.lg.mapper;

import com.github.pagehelper.Page;
import com.lg.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface CheckGroupDao {
    void add(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    Page<CheckGroup> findPage(String queryString);

    CheckGroup findById(Integer id);

    void deleteAssociation(@Param("id") Integer id);

    void edit(CheckGroup checkGroup);

    void deleteById(@Param("id")Integer id);

    Integer findCountById(@Param("id")Integer id);
}
