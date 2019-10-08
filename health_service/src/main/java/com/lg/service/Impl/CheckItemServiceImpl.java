package com.lg.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lg.entity.PageResult;
import com.lg.mapper.CheckItemDao;
import com.lg.pojo.CheckItem;
import com.lg.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @version V1.0
 * @author: lg9288
 * @date: 2019/10/7 15:18
 * @description:
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkItemDao.findCheckItemIdsByCheckGroupId(id);
    }

    /**
     * 查询检查项信息
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();

    }

    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    @Override
    public CheckItem findById(Integer id) {
       return checkItemDao.findById(id);
    }

    /**
     * 根据Id删除表单中的数据
     * @param id
     */
    @Override
    public void deleteById(Integer id) {
        //先查询检查组合检查项是否有关联
        Long result = checkItemDao.findCountByCheckItemId(id);
        if (result > 0) {
            throw new RuntimeException("两表有关联，不能删除");
        }
        //如果为false则可以删除，调用删除方法;
        checkItemDao.deleteById(id);
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
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 添加预约数据
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }
}
