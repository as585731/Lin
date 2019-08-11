package com.yjs.dao;

import com.github.pagehelper.Page;
import com.yjs.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    //添加套餐
    void add(Setmeal setmeal);

    void setSetmealAndCheckGroup(Map<String, Integer> map);

    //分页查询
    Page<Setmeal> selectByCondition(String queryString);

    //根据id，查询对应套餐（回显）
    Setmeal findById(Integer id);

    //根据套餐id，查询对应所有检查组的集合
    List<Integer> findCheckGroupIdsBySetmealId(Integer id);

    //根据检查组id删除中间表数据（清理原有关联关系）
    void deleteAssociation(Integer id);

    //更新检查组基本信息
    void edit(Setmeal setmeal);

    //根据id删除套餐
    void deleteSetmealById(Integer setmealId);

    //获取所有套餐信息（前端）
    List<Setmeal> findAll();

    List<Map<String,Object>> findSetmealCount();
}
