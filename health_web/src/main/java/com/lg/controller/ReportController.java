package com.lg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lg.constant.MessageConstant;
import com.lg.entity.Result;
import com.lg.service.MemberService;
import com.lg.service.OrderService;
import com.lg.service.ReportService;
import com.lg.service.SetMealSerivce;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
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
@RequestMapping("/report")
public class ReportController {
    //打印日志信息
    private static final Logger LOGGER = Logger.getLogger(ReportController.class);
    @Reference
    private MemberService memberService;
    @Reference
    private OrderService orderService;
    @Reference
    private ReportService reportService;
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

    /**
     * echarts饼型图标
     * @return
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {
        try {
            Map<String, Object> result = new HashMap<>();
            //查询套餐的种类，和每种套餐的数量,结果数据封装到list集合中
            List<Map<String,Object>> setmealCount =orderService.getSetmealReport();
            //创建List集合封装name属性
            List<String> setmealNames = new ArrayList<>();
            //遍历setmealCount
            for (Map<String, Object> map : setmealCount) {
                String name = (String) map.get("name");
                setmealNames.add(name);
            }
            result.put("setmealNames", setmealNames);
            result.put("setmealCount",setmealCount);
            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,result);
        } catch (Exception e) {
            LOGGER.error("Get setmeal count error",e);
        }
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
    }

    /**
     * 查询当日，本周，本月的会员及预约到诊人数
     * @return
     */
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            Map map = reportService.getBusinessReportData();
            if (map != null && map.size() > 0) {
                return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
            }
        } catch (Exception e) {
            LOGGER.error("Get business report error",e);
        }
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
    }

    /**
     * 到处excell文档
     */
    @RequestMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletResponse response, HttpServletRequest request) {
        try {
            //获取运营数据
            Map result = reportService.getBusinessReportData();
            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //获取Excel模板的绝对路径
            String templatePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";

            //读取模板获取Excel对象
            XSSFWorkbook workbook = new XSSFWorkbook(templatePath);

            //获取工作表
            XSSFSheet sheet = workbook.getSheetAt(0);
            //获取第二行
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNumb = 12;
            for (Map map : hotSetmeal) {
                String name = (String) map.get("name");//套餐名称
                Long setmeal_count = (Long) map.get("setmeal_count");//预约数量
                BigDecimal proportion = (BigDecimal) map.get("proportion");//占比
                row= sheet.getRow(rowNumb);
                row.getCell(4).setCellValue(name);
                row.getCell(5).setCellValue(setmeal_count);
                row.getCell(6).setCellValue(proportion.toString());
                rowNumb++;
            }


            //设置响应的文件格式
            response.setContentType("application/vnd.ms-excel");
            //设置下载方式
            response.setHeader("content-Disposition","attachment;filename=report.xlsx");
            //获取输出流
            ServletOutputStream out = response.getOutputStream();
            workbook.write(out);//将excel响应出去
            out.flush();//清空缓冲区
            out.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
