package com.yjs.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.yjs.constant.MessageConstant;
import com.yjs.dao.MemberDao;
import com.yjs.dao.OrderDao;
import com.yjs.dao.OrderSettingDao;
import com.yjs.entity.Result;
import com.yjs.pojo.Member;
import com.yjs.pojo.Order;
import com.yjs.pojo.OrderSetting;
import com.yjs.service.OrderService;
import com.yjs.utlis.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
public class OrderServiceImp implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Override
    //提交体检预约
    public Result submitOrder(Map map) throws Exception {
        //1.判断当前日期是否设置了预约
        String orderDate = (String) map.get("orderDate");
        //调用日期工具类，将字符串类型的日期转为date类型
        Date date = DateUtils.parseString2Date(orderDate);
        //查询当前日前是否有设置最大预约数量
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        if (orderSetting == null) {
            //代表还没设置
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2.判断当前日期是否预约满了
        //得到最大可预约人数
        int number = orderSetting.getNumber();
        //得到已预约人数
        int reservations = orderSetting.getReservations();
        //以预约人数是否大于等于最大可预约人数
        if (reservations >= number) {
            //如果大于等于,返回提示
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        //判断当前用户是否是会员
        String telephone = (String) map.get("telephone");
        //根据电话号码，查询用户该电话是否有注册会员
        Member member = memberDao.findByTelephone(telephone);
        //判断
        if (member != null) {
            //代表是会员，判断是否已经预约过（避免重复预约）
            //取出会员的id
            Integer memberId = member.getId();
            //从请求的map中取出当前套餐的id
            int setmealId = Integer.parseInt((String) map.get("setmealId"));
            //新建一个体检预约信息
            Order order = new Order(memberId, date, null, null, setmealId);
            //查询数据库中是否已经有该体检预约信息
            List<Order> list = orderDao.findByCondition(order);
            //判断
            if (list != null && list.size() > 0) {
                //已有数据
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        } else {
            //不是会员（member为null），需要添加到会员表
            //新建一个体检预约对象赋值
            member = new Member();
            //给member对象设置信息
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            //将该对象添加到数据库
            memberDao.add(member);
        }
        //4.进行预约
        //设置预约表的预约人数+1
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        //保存到数据库
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        //保存预约信息到预约表
        //创建一个预约对象
        Order order = new Order(member.getId(),
                date,
                (String) map.get("orderType"),
                Order.ORDERSTATUS_NO,
                Integer.parseInt((String) map.get("setmealId")));
        //执行方法保存
        orderDao.add(order);

        //返回结果集
        return new Result(true, MessageConstant.ORDER_SUCCESS, order);
    }

    @Override
    //根据id查询预约信息，包括体检人信息、套餐信息
    public Map findById4Detail(Integer id) throws Exception {
        //调用dao层进行查询
        Map map = orderDao.findById4Detail(id);
        //非空判断
        if (map!=null){
            //处理日期格式
         Date orderDate = (Date) map.get("orderDate");
         map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
