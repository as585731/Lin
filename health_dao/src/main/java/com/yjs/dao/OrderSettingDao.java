package com.yjs.dao;

import com.yjs.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    //根据日期查询当前日期记录数
    long findCountByOrderDate(Date orderDate);

    //根据预约对象，修改数据库
    void editNumberByOrderDate(OrderSetting orderSetting);

    //根据预约对象，数据库新增数据
    void add(OrderSetting orderSetting);

    //调用dao层进行查询当前月的预约信息（返回一个集合）
    List<OrderSetting> getOrderSettingByMonth(Map map);

    //调用dao层进行查询当前月的预约信息（返回一个Map）
    List<Map> getOrderSettingByMonth2(String date);

    //更新已预约人数
    public void editReservationsByOrderDate(OrderSetting orderSetting);

    //根据预约日期查询预约设置信息
    public OrderSetting findByOrderDate(Date orderDate);

    //根据时间，将其之前的时间的预约历史记录删除
    void cleanHistoryOrder(String date);
}
