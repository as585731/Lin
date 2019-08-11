package com.yjs.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yjs.constant.MessageConstant;
import com.yjs.entity.PageResult;
import com.yjs.entity.QueryPageBean;
import com.yjs.entity.Result;
import com.yjs.pojo.CheckItem;
import com.yjs.service.CheckItemService;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//体检检查项管理
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    //新增
    @RequestMapping("/add.do")
    public Result add(@RequestBody CheckItem checkItem){
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            //报错，结果集设置为false，封装入提示信息
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        //成功，结果集设置为true，封装入提示信息
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findPage")
    //分页查询
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        //调用service层的方法进行查询
        PageResult pageResult = checkItemService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        //响应结果集
        return pageResult;
    }

    //删除
    @RequestMapping("/delete")
    public Result delete(Integer id){
        //调用service层的方法进行删除处理
        try {
            checkItemService.delete(id);
        }catch (RuntimeException e){
            //如果运行报错，结果集设置false，传入抛出的提示信息（当前检查项被引用）
            return new Result(false,e.getMessage());
        }catch (Exception e){
            //其他错误设置
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        //成功，设置true，传入提示信息
        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    //根据指定id查询检查检查项
    @RequestMapping("findById")
    public Result findById(Integer id){
        //调用service层，以传入的check的id查询出数据
        try {
            CheckItem checkItem = checkItemService.findById(id);
            //执行成功
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
        }catch (Exception e){
            //执行失败
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }

    }

    //编辑
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem){
        //调用service层，以传入的check对象进行数据更新
        try {
            checkItemService.edit(checkItem);
        }catch (Exception e){
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    //查询所有检查项
    @RequestMapping("/findAll")
    public Result findAll(){
        //调用业务层，查询出所有检查项的list集合
       List<CheckItem> checkItemList =checkItemService.findAll();
       //判断集合不为空或者长度大于0
        if(checkItemList != null && checkItemList.size() > 0){
            //查询成功，封装入结果集对象返回
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemList);
        }
        //如果为空或者长度为0，代表查询失败
        return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);

    }

}
