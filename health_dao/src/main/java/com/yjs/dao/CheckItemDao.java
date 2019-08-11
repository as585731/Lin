package com.yjs.dao;

import com.github.pagehelper.Page;
import com.yjs.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    //根据表单提交的数据新增检查项
    void add(CheckItem checkItem);

    //进行分页查询
    Page<CheckItem> selectByCondition(String queryString);

    //查询当前检查项是否和检查组关联
    long findCountByCheckItemId(Integer id);

    //根据id删除指定检查项
    void deleteById(Integer id);

    //以传入的check对象进行数据更新
    void edit(CheckItem checkItem);

    //根据指定id查询检查检查项
    CheckItem findById(Integer id);

    //查询所有检查项
    List<CheckItem> findAll();

    //根据检查组id查询检查项信息
//    List<CheckItem>findCheckItemById(Integer id);

}
