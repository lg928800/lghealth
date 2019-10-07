package com.lg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lg.constant.MessageConstant;
import com.lg.entity.PageResult;
import com.lg.entity.QueryPageBean;
import com.lg.entity.Result;
import com.lg.pojo.CheckItem;
import com.lg.service.CheckItemService;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version V1.0
 * @author: lg9288
 * @date: 2019/10/7 15:13
 * @description:
 */
@RestController
@RequestMapping("/checkItem")
public class CheckItemController {

    private static final Logger LOGGER = Logger.getLogger(CheckItemController.class);
    @Reference
    private CheckItemService checkItemService;

    /**
     * 添加数据，新建预约
     * @param checkItem
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem) {
        //接受参数,调用service层方法
        try {
            checkItemService.add(checkItem);
            return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            //打印错误信息
            LOGGER.error("Add checkitem error.",e);
            return new Result(false,MessageConstant.ADD_CHECKITEM_FAIL);
        }
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping("findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkItemService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
        return pageResult;
    }

    /**
     * 根据ID删除数据
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public Result deleteById(Integer id) {
        try {
            checkItemService.deleteById(id);
            return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
        } catch (RuntimeException e) {
            LOGGER.error("delete checkitem error",e);
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        } catch (Exception e) {
            LOGGER.error("delete checkitem cause error",e);
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }

    /**
     * 根据id查询数据
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            CheckItem checkItem = checkItemService.findById(id);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
        } catch (Exception e) {
            LOGGER.error("Find checkitem by id cause error." ,e);
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /**
     * 编辑框内修改数据
     * @param checkItem
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.edit(checkItem);
            return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            LOGGER.error("Edite checkitem by id cause error.",e);
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }

    }
}
