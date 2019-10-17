package com.lg.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lg.Utils.MD5Utils;
import com.lg.mapper.MemberDao;
import com.lg.pojo.Member;
import com.lg.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @version V1.0
 * @author: lg9288
 * @date: 2019/10/10 16:14
 * @description:
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberSerivceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;


    @Override
    public Map getMemberReport() {
        //获取日期时间
        Calendar instance = Calendar.getInstance();
        //把日期往前推进12个月
        instance.add(Calendar.MONTH,-12);
        //定义两个集合封装月份和会员数量的数据
        List<String> months = new ArrayList<>();
        //会员数量
        List<Integer> membercounts = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            //获取当前的时间
            Date time = instance.getTime();

            //获取每个月的月份
            String month = new SimpleDateFormat("yyyy-MM").format(time);
            //定义月份开始时间
            String monthBegin = month+"-1";
            String monthEnd = month+"-31";
            //查询每个月份的会员数量
            int count = memberDao.findByMonthCount(monthBegin, monthEnd);
            //将数据添加到集合中
            months.add(month);//月份
            membercounts.add(count);//会员数量

            //每次循环月份增加一个月
            instance.add(Calendar.MONTH,+1);
        }
        Map<String,List> map = new HashMap<>();
        map.put("months",months);
        map.put("memberCounts",membercounts);
        return map;
    }

    @Override
    public void check(Map map) {
        //获取电话号码的值，查询是否是会员
        String telephone = (String) map.get("telephone");
        if (telephone != null) {
            Member member = memberDao.findByTelephone(telephone);
            //判断Member是否是Null
            if (member == null) {
                //如果为空，则添加为新的会员
                member=new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                if (member.getPassword() != null) {
                    String password = MD5Utils.md5(member.getPassword());
                    member.setPassword(password);
                }
                memberDao.add(member);
            }
        }
    }
}
