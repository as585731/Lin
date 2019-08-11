package com.yjs.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.yjs.dao.OrderSettingDao;
import com.yjs.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;


    @Override
    //根据传进来的对象进行更新或者保存（批量）
    public void add(List<OrderSetting> orderSettingList) {
        //循环遍历
        for (OrderSetting orderSetting : orderSettingList) {
            //检查此数据（日期）是否存在
            long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
            if (count > 0) {
                //已经存在,执行更新操作
                orderSettingDao.editNumberByOrderDate(orderSetting);
            } else {
                //不存在，执行添加操作
                orderSettingDao.add(orderSetting);
            }

        }
    }

/*    @Override
    //根据传进来的月份，查询当前月每天的预约信息
    public List<Map> getOrderSettingByMonth(String date) {
        //设置当前月的起始日
        String dateBegin = date+"-1";
        //设置当前月的结束日
        String dateEnd = date+"-31";
        //创建一个map，用来封装起始日和结束日，方便dao层查询时对应取数据
        Map map = new HashMap();
        map.put("dateBegin",dateBegin);
        map.put("dateEnd",dateEnd);

        //调用dao层进行查询当前月的预约信息（返回一个集合）
        List<OrderSetting> list =  orderSettingDao.getOrderSettingByMonth(map);
        //新建一个List<Map>集合,内里每个map对应封装日期、可预约人数、已预约人数，方便前端获取
        List<Map> data = new ArrayList<>();
        //遍历查询出来的list集合，将里面的日期、可预约人数，已预约人数，拆分出来存入map集合
        for (OrderSetting orderSetting : list) {
            //新建一个map用来存数据
            Map orderSettingMap = new HashMap();
            //分别取出需要的数据存入map
            orderSettingMap.put("date",orderSetting.getOrderDate().getDate());//获得日期（几号）
            orderSettingMap.put("number",orderSetting.getNumber());//可预约人数
            orderSettingMap.put("reservations",orderSetting.getReservations());//已预约人数
            //存入list集合
            data.add(orderSettingMap);
        }
        //返回这个集合
        System.out.println(data);
        return data;
    }*/

    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        return orderSettingDao.getOrderSettingByMonth2(date);
    }

    @Override
    //根据指定日期修改可预约人数
    public void editNumberByDate(OrderSetting orderSetting) {
        //查询当前日期是否有设置过
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        //判断
        if (count>0) {
            //大于0，代表已经进行过了预约设置，进行更新操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            //没有设置过，进行添加操作
            orderSettingDao.add(orderSetting);
        }

    }

    @Override
    //根据时间，将其之前的时间的预约历史记录删除
    public void cleanHistoryOrder(String date) {
        orderSettingDao.cleanHistoryOrder(date);
    }

}
