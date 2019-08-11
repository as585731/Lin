package com.yjs.service;

import com.yjs.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    //根据传进来的对象进行更新或者保存
    void add(List<OrderSetting> orderSettingList);

    //根据传进来的月份，查询当前月每天的预约信息
    List<Map> getOrderSettingByMonth(String date);

    //根据指定日期修改可预约人数
    void editNumberByDate(OrderSetting orderSetting);

    //根据时间，将其之前的时间的预约历史记录删除
    void cleanHistoryOrder(String date);
}
