package com.yjs.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.yjs.constant.MessageConstant;
import com.yjs.constant.RedisMessageConstant;
import com.yjs.entity.Result;
import com.yjs.pojo.Order;
import com.yjs.service.OrderService;
import com.yjs.utlis.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

//体检预约
@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;

    //提交预约的方法
    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody Map map){
        //从请求中拿到手机号码
        String telephone = (String) map.get("telephone");
        //从Redis中获取缓存的验证码，key为手机号+redisConstant.SENDTYPE_ORDER
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //从请求中拿到用户输入的验证码
        String validateCode = (String) map.get("validateCode");
        //校验该验证码
        if (codeInRedis == null || !codeInRedis.equals(validateCode)) {
            //代表redis内没有该手机的验证码或者验证码不正确
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        Result result = null;
        //存入预约类型
        map.put("ordrType", Order.ORDERTYPE_WEIXIN);
        //调用体检预约服务
        try {
            result = orderService.submitOrder(map);
        } catch (Exception e) {
            e.printStackTrace();
            //预约失败
            return result;
        }
        if (result.isFlag()) {
            //从map中拿到体检日期
            String orderDate = (String) map.get("orderDate");
            //预约成功，发送短信通知
            try {
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,orderDate);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
        //返回result
        return result;

    }


     //根据id查询预约信息，包括套餐信息和会员信息
    @RequestMapping("/findById")
    public Result findById(Integer id){
        Result result =null;
        try{
            //调用业务层，根据id查询预约信息，包括体检人信息、套餐信息
            Map map = orderService.findById4Detail(id);
            //查询预约信息成功
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            //查询预约信息失败
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }


}
