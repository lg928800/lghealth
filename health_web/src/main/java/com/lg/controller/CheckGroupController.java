package com.lg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lg.constant.MessageConstant;
import com.lg.entity.PageResult;
import com.lg.entity.QueryPageBean;
import com.lg.entity.Result;
import com.lg.pojo.CheckGroup;
import com.lg.pojo.CheckItem;
import com.lg.service.CheckGroupService;
import com.lg.service.CheckItemService;
import org.apache.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: lg9288
 * @date: 2019/10/7 15:13
 * @description:
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    //打印日志信息
    private static final Logger LOGGER = Logger.getLogger(CheckGroupController.class);
    //创建service层业务接口
    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 添加检查组信息
     * @param checkitemIds
     * @param checkGroup
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestParam Integer[] checkitemIds,@RequestBody CheckGroup checkGroup) {
        //调用service层方法添加信息
        try {
            checkGroupService.add(checkitemIds,checkGroup);
            return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            LOGGER.error("add checkGroup error",e);
        }
            return new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
       return checkGroupService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());

    }

    /**
     * 根据ID查询检查组的数据
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById( Integer id) {
        try {
            CheckGroup checkGroup = checkGroupService.findById(id);
            if (checkGroup != null) {
                return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
            }
        } catch (Exception e) {
            LOGGER.error("Find checkgroup by id error",e);
        }
                return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

    /**
     * 编辑框确认后，修改数据
     * @param checkitemIds
     * @param checkGroup
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_EDIT')")
    @RequestMapping("/edit")
    public Result edit(@RequestParam Integer[] checkitemIds, @RequestBody CheckGroup checkGroup) {
        try {
            checkGroupService.edit(checkitemIds,checkGroup);
            //修改成功
            return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            LOGGER.error("Edit checkGroup error",e);
        }
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
    }

    /**
     * 根据ID删除数据
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_DELETE')")
    @RequestMapping("/deleteById")
    public Result deleteById(@RequestParam("id")Integer id) {
        try {
            checkGroupService.deleteById(id);
            return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        } catch (RuntimeException e) {
            LOGGER.error("error",e);
            return new Result(false,e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Delete checkgroup by id error", e);
        }
        return  new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
    }

    /**
     * 查询检查组数据并回显到页面
     * @return
     */
    @GetMapping("/findAll")
    public Result findAll() {
        //查询检查组数据
        try {
            List<CheckGroup> checkGroups = checkGroupService.findAll();
            if (checkGroups != null) {
                return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroups);
            }
        } catch (Exception e) {
            //日志打印;
            LOGGER.error("Find checkgroup all ",e);
        }
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
    }
}
