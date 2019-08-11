package com.yjs.controller.authority;


import com.alibaba.dubbo.config.annotation.Reference;
import com.yjs.constant.MessageConstant;
import com.yjs.entity.PageResult;
import com.yjs.entity.QueryPageBean;
import com.yjs.entity.Result;
import com.yjs.pojo.CheckItem;
import com.yjs.pojo.Permission;
import com.yjs.service.CheckItemService;
import com.yjs.service.authority.PermissionService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//权限管理
@RestController
@RequestMapping("/permission")
public class PermissionController {
    
    @Reference
    private PermissionService permissionService;

    //新增
    @RequestMapping("/add.do")
    public Result add(@RequestBody Permission permission){
        try {
            permissionService.add(permission);
        } catch (Exception e) {
            //报错，结果集设置为false，封装入提示信息
            return new Result(false, "新增权限失败");
        }
        //成功，结果集设置为true，封装入提示信息
        return new Result(true,"新增权限成功");
    }

    @RequestMapping("/findPage")
    //分页查询
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        //调用service层的方法进行查询
        PageResult pageResult = permissionService.pageQuery(
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
            permissionService.delete(id);
        }catch (RuntimeException e){
            //如果运行报错，结果集设置false，传入抛出的提示信息（当前权限被引用）
            return new Result(false,e.getMessage());
        }catch (Exception e){
            //其他错误设置
            return new Result(false, "删除权限失败");
        }
        //成功，设置true，传入提示信息
        return new Result(true,"删除权限成功");
    }

    //根据指定id查询检查权限
    @RequestMapping("findById")
    public Result findById(Integer id){
        //调用service层，以传入的check的id查询出数据
        try {
            Permission permission = permissionService.findById(id);
            //执行成功
            return new Result(true,"查询权限成功",permission);
        }catch (Exception e){
            //执行失败
            return new Result(false,"查询权限失败");
        }

    }

    //编辑
    @RequestMapping("/edit")
    public Result edit(@RequestBody Permission permission){
        //调用service层，以传入的check对象进行数据更新
        try {
            permissionService.edit(permission);
        }catch (Exception e){
            return new Result(false,"编辑权限失败");
        }
        return new Result(true,"编辑权限成功");
    }

    //查询所有权限
    @RequestMapping("/findAll")
    public Result findAll(){
        //调用业务层，查询出所有权限的list集合
        List<Permission> permissionList =permissionService.findAll();
        //判断集合不为空或者长度大于0
        if(permissionList != null && permissionList.size() > 0){
            //查询成功，封装入结果集对象返回
            return new Result(true,"查询权限成功",permissionList);
        }
        //如果为空或者长度为0，代表查询失败
        return new Result(false,"查询权限失败");

    }
    
}
