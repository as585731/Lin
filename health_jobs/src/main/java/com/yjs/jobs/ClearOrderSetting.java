package com.yjs.jobs;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yjs.service.OrderSettingService;
import com.yjs.utlis.DateUtils;

import java.util.Calendar;

//删除多余数据库历史信息
public class ClearOrderSetting {
    @Reference
    OrderSettingService orderSettingService;

    //定时清理上上月的可预约人数
    public void cleanHistoryOrder(){

        //创建一个日历对象
        Calendar calendar = Calendar.getInstance();
        //将日历对象的月份字段，设置为上个月前
        calendar.add(Calendar.MONTH,-1);
        try {
            //将日历对象转为date再转为字符串
            String date = DateUtils.parseDate2String(calendar.getTime());
            System.out.println("date:"+date);
            //调用业务层，将上个月之前的可预约人数删除
            orderSettingService.cleanHistoryOrder(date);
            System.out.println("历史记录删除成功");
        } catch (Exception e) {
            System.out.println("历史记录删除失败");
            e.printStackTrace();
        }

    }
}
