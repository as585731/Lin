package com.yjs.service;

import com.yjs.entity.PageResult;
import com.yjs.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {

    //根据表单提交的数据新增检查项
    void add(CheckItem checkItem);

    //进行分页查询
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    //根据id删除指定检查项
    void delete(Integer id);

    //以传入的check对象进行数据更新
    void edit(CheckItem checkItem);

    //根据指定id查询检查检查项
    CheckItem findById(Integer id);

    //查询所有检查项
    List<CheckItem> findAll();
}
