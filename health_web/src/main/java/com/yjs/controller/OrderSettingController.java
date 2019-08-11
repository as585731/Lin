package com.yjs.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yjs.constant.MessageConstant;
import com.yjs.entity.Result;
import com.yjs.pojo.OrderSetting;
import com.yjs.service.OrderSettingService;
import com.yjs.utlis.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;

    //Excel文件上传，并解析文件内容保存到数据库
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile")MultipartFile excelFile) {
        try {
            //读取并解析excel文件，将文件每行作为一个数组，所有数组作为一个集合
            List<String[]> list = POIUtils.readExcel(excelFile);
            //判断该集合是否不为空
            if (list!=null&&list.size()>0) {
                //新建一个OrderSetting对象的集合对象
                List<OrderSetting> orderSettingList = new ArrayList<>();
                //循环遍历list集合
                for (String[] strings : list) {
                    //创建一个OrderSetting对象，使用有参构造方法，分别赋值日期和预约次数
                    OrderSetting orderSetting = new OrderSetting(new Date(strings[0]), Integer.parseInt(strings[1]));
                    //把这个OrderSetting对象存入集合
                    orderSettingList.add(orderSetting);
                }
                //调用业务层对象进行更新或者保存
                orderSettingService.add(orderSettingList);
            }
        } catch (IOException e) {
            //失败返回
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        //成功返回
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);

    }

    //根据日期查询预约数
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){//参数格式为：2019-03
        try{
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            //获取预约设置数据成功
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            //获取预约设置数据失败
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }


    //根据指定日期修改可预约人数
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try{
            //调用业务层方法进行新增或者编辑
            orderSettingService.editNumberByDate(orderSetting);
            //预约设置成功
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            //预约设置失败
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
    

}
