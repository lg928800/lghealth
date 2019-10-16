package com.lg.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lg.Utils.DateUtils;
import com.lg.Utils.MD5Utils;
import com.lg.constant.MessageConstant;
import com.lg.mapper.MemberDao;
import com.lg.mapper.OrderDao;
import com.lg.mapper.OrderSettingDao;
import com.lg.pojo.Member;
import com.lg.pojo.Order;
import com.lg.pojo.OrderSetting;
import com.lg.service.MemberSerivce;
import com.lg.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * @version V1.0
 * @author: lg9288
 * @date: 2019/10/10 16:14
 * @description:
 */
@Service(interfaceClass = MemberSerivce.class)
@Transactional
public class MemberSerivceImpl implements MemberSerivce {
    @Autowired
    private MemberDao memberDao;


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
