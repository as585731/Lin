package com.yjs.service.authority;

import com.yjs.entity.PageResult;
import com.yjs.pojo.Menu;

import java.util.List;
import java.util.Map;

public interface MenuService {

    //查询user对象对应的菜单
    List<Menu> selectByUser(String username);

    //根据表单提交的数据新增菜单
    void add(Menu menu);

    //进行分页查询
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    //根据id删除指定菜单
    void delete(Integer id);

    //以传入的菜单对象进行数据更新
    void edit(Menu menu);

    //根据指定id查询菜单
    Menu findById(Integer id);

    //查询所有菜单
    List<Menu> findAll();

    //查询所有level为1的菜单的id
    List<Map> selectParentMenuIds();
    
}
