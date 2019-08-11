package com.yjs.service.authority;

import com.yjs.entity.PageResult;
import com.yjs.pojo.CheckItem;
import com.yjs.pojo.Permission;

import java.util.List;

public interface PermissionService {

    //根据表单提交的数据新增权限
    void add(Permission permission);

    //进行分页查询
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    //根据id删除指定权限
    void delete(Integer id);

    //以传入的权限对象进行数据更新
    void edit(Permission permission);

    //根据指定id查询权限
    Permission findById(Integer id);

    //查询所有权限
    List<Permission> findAll();
}
