package com.yjs.service;

import com.yjs.entity.PageResult;
import com.yjs.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    //新增套餐组
    void add(Setmeal setmeal, Integer[] checkGroupIds);

    //分页查询
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    //根据id，查询对应套餐（回显）
    Setmeal findById(Integer id);

    //根据套餐id，查询对应所有检查组的集合
    List<Integer> findCheckGroupIdsBySetmealId(Integer id);

    //编辑提交，根据数据修改检查组的表
    void edit(Setmeal setmeal, Integer[] checkGroupIds);

    //根据id删除套餐
    void delete(Integer id);


    //获取所有套餐信息（前端）
    List<Setmeal> findAll();

    //
    List<Map<String,Object>> findSetmealCount();

}
