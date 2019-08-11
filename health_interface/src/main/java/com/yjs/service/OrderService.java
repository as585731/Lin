package com.yjs.service;

import com.yjs.entity.Result;

import java.util.Map;

public interface OrderService {
    //提交体检预约
    Result submitOrder(Map map) throws Exception;

    //根据id查询预约信息，包括体检人信息、套餐信息
    Map findById4Detail(Integer id) throws Exception;
}
