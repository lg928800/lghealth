package com.lg.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lg.Utils.DateUtils;
import com.lg.constant.MessageConstant;
import com.lg.mapper.MemberDao;
import com.lg.mapper.OrderDao;
import com.lg.mapper.OrderSettingDao;
import com.lg.mapper.ReportDao;
import com.lg.pojo.Member;
import com.lg.pojo.Order;
import com.lg.pojo.OrderSetting;
import com.lg.service.OrderService;
import com.lg.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: lg9288
 * @date: 2019/10/10 16:14
 * @description:
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportSerivceImpl implements ReportService {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
    /**
     * 查询运营数据统计
     * @return
     * reportDate:今天的日期
     * todayNewMember :今日新增会员数,
     * totalMember :总会员数,
     * thisWeekNewMember :本周新增会员数,
     * thisMonthNewMember :本月新增会员数,
     * todayOrderNumber :今日预约人数,
     * todayVisitsNumber :今日到诊人数,
     * thisWeekOrderNumber :本周预约人数,
     * thisWeekVisitsNumber :本周到诊人数,
     * thisMonthOrderNumber :本月预约人数,
     * thisMonthVisitsNumber :本月到诊人数,
     * hotSetmeal 热门套餐:
     */
    @Override
    public Map getBusinessReportData() throws Exception {
        //1.查询今天的日期
        String today = DateUtils.parseDate2String(DateUtils.getToday());
        //获取本周的周一
        String Monday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        //获取本月的第一天
        String firstDay4ThisMonth  = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        //2.根据日期查询今日新增会员数
        Integer todayNewMember = memberDao.findByTodayMember(today);
        //3.查询总的会员数
        Integer totalMember = memberDao.findAllMember();
        //4.查询一周新增会员数
        Integer thisWeekNewMember =memberDao.findNewMemberCountAfterDate(Monday);
        //5.查询本月新增会员数

        Integer thisMonthNewMember  = memberDao.findNewMemberCountAfterDate(firstDay4ThisMonth);
        //6.根据今日日期查询预约人数
        Integer todayOrderNumber =orderDao.findTodayOrderNumber(today);
        //7.本周预约人数
        Integer thisWeekOrderNumber = orderDao.findOrderNumberAfterDate(Monday);
        //8.本月预约人数
        Integer thisMonthOrderNumber  = orderDao.findOrderNumberAfterDate(firstDay4ThisMonth);
        //9.今日到诊人数
        Integer todayVisitsNumber =orderDao.findTodayVisitsNumber(today);
        //10.本周到诊人数
        Integer thisWeekVisitsNumber = orderDao.findVisitsNumberAfterDate(Monday);
        //11.本月到诊人数
        Integer thisMonthVisitsNumber  = orderDao.findVisitsNumberAfterDate(firstDay4ThisMonth);
        //12.热门套餐
        List<Map> hotSetMeal = orderDao.findHotSetmeal();
        Map<String, Object> result = new HashMap<>();
        result.put("reportDate",today);
        result.put("todayNewMember",todayNewMember);
        result.put("totalMember",totalMember);
        result.put("thisWeekNewMember", thisWeekNewMember);
        result.put("thisMonthNewMember",thisMonthNewMember);
        result.put("todayOrderNumber",todayOrderNumber);
        result.put("thisWeekOrderNumber",thisWeekOrderNumber);
        result.put("thisMonthOrderNumber",thisMonthOrderNumber);
        result.put("todayVisitsNumber",todayVisitsNumber);
        result.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        result.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        result.put("hotSetmeal",hotSetMeal);
        return result;
    }
}
