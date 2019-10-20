package com.lg.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lg.constant.MessageConstant;
import com.lg.entity.PageResult;
import com.lg.mapper.CheckGroupDao;
import com.lg.pojo.CheckGroup;
import com.lg.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: lg9288
 * @date: 2019/10/8 15:31
 * @description:
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    //创建dao层接口实现
    @Autowired
    private CheckGroupDao checkGroupDao;
    @Autowired
    private JedisPool jedisPool;

    /**
     * 查询检查组所有数据
     */
    @Override
    public List<CheckGroup> findAll() {
/*        Jedis jedis = jedisPool.getResource();
        String data = jedis.get("SetMeal_Data");
        if (data == null) {
            List<CheckGroup> list = checkGroupDao.findAll();
            jedis.set("SetMeal_Data",list.toString());
        }*/
        return checkGroupDao.findAll();

    }

    /**
     * 根据ID删除数据
     * @param id
     */
    @Override
    public void deleteById(Integer id) {
        //查询是否被套餐引用
        Integer count = checkGroupDao.findCountById(id);
        //判断count是否大于0
        if (count > 0) {
            throw new RuntimeException(MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        }
        //先删除两表之间的关联关系
        checkGroupDao.deleteAssociation(id);
        //再删除需要删除的检查组
        checkGroupDao.deleteById(id);
    }

    /**
     * 编辑确定业务，删除两边之间的关系，并更新返回
     * @param checkitemIds
     * @param checkGroup
     */
    @Override
    public void edit(Integer[] checkitemIds, CheckGroup checkGroup) {
        //dao层调用删除方法删除两表之间的关系
        checkGroupDao.deleteAssociation(checkGroup.getId());
        //重新编辑后建立更新两表之间的关系
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
        //具体修改层,调用修改方法
        checkGroupDao.edit(checkGroup);
    }

    /**
     * 编辑选项根据ID回显检查组数据
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(Integer id) {

        return checkGroupDao.findById(id);
    }

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //调用pagehelper中的startPage方法
        PageHelper.startPage(currentPage,pageSize);
        //dao层查询数据
        Page<CheckGroup> page = checkGroupDao.findPage(queryString);

        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 添加检查组信息
     * @param checkitemIds
     * @param checkGroup
     */
    @Override
    public void add(Integer[] checkitemIds, CheckGroup checkGroup) {
        checkGroupDao.add(checkGroup);
        //设置检查组和检查项的关系
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
    }

    /**
     * 设置检查组和检查项的关系
     * @param id
     * @param checkitemIds
     */
    private void setCheckGroupAndCheckItem(Integer id, Integer[] checkitemIds) {
        //判断id数组是否为空
        if (checkitemIds != null && checkitemIds.length > 0) {
            //遍历ID数据，并封装到MAP集合中
            for (Integer ids : checkitemIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("checkgroup_id",id);
                map.put("checkitem_id",ids);
                //调用dao层设置关系
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }



        }


    }
}
