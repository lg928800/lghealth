package com.lg.jobs;

import com.lg.Utils.DateUtils;
import com.lg.mapper.OrderSettingDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @version V1.0
 * @author: lg9288
 * @date: 2019/10/21 9:47
 * @description:
 */
public class DeleteOrderSettingAllOfDate {
    //获取orderSettingDao层的对象
    @Autowired
    private OrderSettingDao orderSettingDao;//这里可能是idea的工具问题

    /**
     * 定时删除当月预约的数据
     */
    public void QuartzDeleteOrderSettingData() throws Exception {
        //1.根据工具类获取当月的第一天(并转换成字符串类型)
        String FirstDayThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        //2.根据当月的第一天，调用持久层根据定时时间删除当月数据
        if (FirstDayThisMonth != null) {
            orderSettingDao.deleteOrderSettingOfDate(FirstDayThisMonth);
        }
    }
}
