package com.lg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lg.constant.MessageConstant;
import com.lg.entity.Result;
import com.lg.service.MemberService;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @version V1.0
 * @author: lg9288
 * @date: 2019/10/7 15:13
 * @description:
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    //打印日志信息
    private static final Logger LOGGER = Logger.getLogger(ReportController.class);
    @Reference
    private MemberService memberService;
    /**
     * 编写统计框会员统计
     *
     * @return
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        try {
            Map map = memberService.getMemberReport();
            System.out.println(map);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            LOGGER.error("Get member repot error",e);
        }
        return new Result(false,MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
    }
}
