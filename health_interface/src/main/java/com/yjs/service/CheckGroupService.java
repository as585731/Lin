package com.yjs.service;

import com.yjs.entity.PageResult;
import com.yjs.pojo.CheckGroup;
import com.yjs.pojo.CheckItem;

import java.util.List;

public interface CheckGroupService {
    //新增检查组
    void add(CheckGroup checkGroup, Integer[] checkitemIds);
    //分页查询
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    //根据id查询检查组的信息，用于编辑的回显
    CheckGroup findById(Integer id);

    //根据检查组合id查询对应的所有检查项的id
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    //编辑提交，根据数据修改检查组的表
    void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    //根据id删除检查组
    void delete(Integer CheckGroupId);

    //查询所有检查组
    List<CheckGroup> findAll();

}
