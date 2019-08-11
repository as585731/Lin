package com.yjs.dao;

import com.github.pagehelper.Page;
import com.yjs.pojo.CheckGroup;
import com.yjs.pojo.CheckItem;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
    //新增检查组
    void add(CheckGroup checkGroup);

    //设置检查组合和检查项的关联关系（设置中间表）
    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    //按条件查询检查组的表
    Page<CheckGroup> selectByCondition(String queryString);

    //调用dao层根据id进行查询，返回对象
    CheckGroup findById(Integer id);

    //调用dao层根据检查组id查询对应所有检查项的id
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    //根据检查组id删除中间表数据（清理原有关联关系）
    void deleteAssociation(Integer id);

    //更新检查组基本信息
    void edit(CheckGroup checkGroup);

    //根据id删除对应检查组的字段
    void deleteCheckGroupById(Integer checkGroupId);

    //查询所有检查组
    List<CheckGroup> findAll();

    //根据id查询所有检查组
//    List<CheckGroup> findCheckGroupById(Integer id);

}
