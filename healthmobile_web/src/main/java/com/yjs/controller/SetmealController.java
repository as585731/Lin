package com.yjs.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yjs.constant.MessageConstant;
import com.yjs.entity.Result;
import com.yjs.pojo.Setmeal;
import com.yjs.service.SetmealService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference//(check = false)
    private SetmealService setmealService;

    //获取所有套餐信息
    @RequestMapping("/getSetmeal")
    public Result getSetmeal(){
        try{
            List<Setmeal> list = setmealService.findAll();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true,MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    //根据id查询套餐信息
    @RequestMapping("/findById")
    public Result findById(int id){
        try{
            //调用业务层的方法根据id得到套餐对象
            Setmeal setmeal = setmealService.findById(id);
            //成功的封装
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        }catch (Exception e){
            e.printStackTrace();
            //失败的封装
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}