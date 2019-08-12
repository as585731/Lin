package com.yjs.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yjs.constant.MessageConstant;
import com.yjs.entity.Result;
import com.yjs.service.MemberService;
import com.yjs.service.ReportService;
import com.yjs.service.SetmealService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//统计表
@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;
    
    
    //会员数量统计
    /*@RequestMapping("/getMemberReport")
    public Result getMemberReport(String value1){

        //调用service层
        Map<String, Object> map = null;
        try {
            map = memberService.findMemberCountByMonth(value1);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
        //返回结果集
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }*/

    //会员数量统计(前12月)
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -12);// 获得当前日期之前12个月的日期

        List<String> list = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            list.add(new SimpleDateFormat("yyyy.MM").format(calendar.getTime()));
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("months", list);

        List<Integer> memberCount = memberService.findMemberCountByMonth(list);
        map.put("memberCount", memberCount);

        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
    }
    // 根据月份动态获取会员数量
    @RequestMapping("/countUserDynamic")
    public Result countUserDynamic(String startDate,String endDate) {
        try {
            Map<String,Object> map = memberService.countUserDynamic(startDate,endDate);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }
    
    //套餐占比统计
    @RequestMapping("/getSetmealSexReport")
    public Result getSetmealReport(){
       // 调用业务层，查询当前所有预约信息，返回一个list，list内存有一个map集合，
        //该map集合内有两个key，分别是name和value，name存放套餐的名字，value存放套餐的预约数量

        List<Map<String, Object>> list = setmealService.findSetmealCount();

        //把list存入map，该map供前端页面的series字段的data属性接收，data属性是一个集合，
        //，集合内的每个元素代表饼图的一个分类，每个元素有value属性，和name属性，分别代表数量和名字

        Map<String,Object> map = new HashMap<>();
        map.put("setmealCount",list);

        //将map内的name取出来放到一个list集合中，在将list集合存入map中，key为setmealNames，
        //前端页面legend字段的data属性接收该属性，该属性是一个list集合，存储饼图分类的示例图，需要和饼图上的分类数据一一对应



        List<String> setmealNames = new ArrayList<>();
        for(Map<String,Object> m : list){
            String name = (String) m.get("name");
            setmealNames.add(name);
        }
        map.put("setmealNames",setmealNames);

        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
    }


    //获取运营统计数据
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String, Object> result = reportService.getBusinessReport();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,result);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 导出Excel报表
     * @return
     */
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try{
            //远程调用报表服务获取报表数据
            Map<String, Object> result = reportService.getBusinessReport();

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

            //获得Excel模板文件绝对路径
            String temlateRealPath = request.getSession().getServletContext().getRealPath("template") +
                    File.separator + "report_template.xlsx";

            //读取模板文件创建Excel表格对象
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(temlateRealPath)));
            XSSFSheet sheet = workbook.getSheetAt(0);

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

            int rowNum = 12;
            for(Map map : hotSetmeal){//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //通过输出流进行文件下载
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(out);

            out.flush();
            out.close();
            workbook.close();

            return null;
        }catch (Exception e){
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL,null);
        }
    }


    @RequestMapping("/getSetmealReport")
    //统计男女会员数量占比和各年龄段会员占比
    public Result getMember(){
        
        //调用业务层，查询男女会员的数量，返回一个list，list内存有两个map集合（男和女），
        // 该map集合内有两个key，分别是name和value，name存放男和女，value存放男和女的数量
        List<Map<String, Object>> list = memberService.selectMemberSex();
        
        //把list存入map，该map供前端页面的series字段的data属性接收，data属性是一个集合，
        //，集合内的每个元素代表饼图的一个分类，每个元素有value属性，和name属性，分别代表数量和名字
        
        Map<String,Object> sexMap = new HashMap<>();
        sexMap.put("setmealCountSex",list);

        //将map内的name取出来放到一个list集合中，在将list集合存入map中，key为setmealNames，
        //前端页面legend字段的data属性接收该属性，该属性是一个list集合，存储饼图分类的示例图，需要和饼图上的分类数据一一对应
         
        List<String> setmealNames = new ArrayList<>();
        for(Map<String,Object> m : list){
            String name = (String) m.get("name");
            setmealNames.add(name);
        }
        
        
        //调用业务层，查询出各年龄段的会员对象
        List<Map<String, Object>> ages = memberService.selectMemberAge();
        Map<String,Object> ageMap = new HashMap<>();
        ageMap.put("setmealCountAge",ages);
        
       
        //存入集合
        for(Map<String,Object> m : ages){
            String name = (String) m.get("name");
            setmealNames.add(name);
        }
        
        //将两个map存入一个新map
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("sex",sexMap);
        resultMap.put("age",ageMap);
        resultMap.put("setmealNames",setmealNames);
        
        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,resultMap);
    }
    
}
