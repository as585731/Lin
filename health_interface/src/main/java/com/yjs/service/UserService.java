package com.yjs.service;

import com.yjs.entity.PageResult;
import com.yjs.pojo.User;
import com.yjs.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    //根据用户名查询user对象
    User findUserByUsername(String username);

    //新增用户组
    void add(User user, Integer[] roleIds);

    //分页查询
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) throws Exception;

    //根据id，查询对应用户（回显）
    User findById(Integer id);

    //根据用户id，查询对应所有角色的集合
    List<Integer> findRoleIdsByUserId(Integer id);

    //编辑提交，根据数据修改角色的表
    void edit(User user, Integer[] roleIds);

    //根据id删除用户
    void delete(Integer id);


    //获取所有用户信息（前端）
    List<User> findAll();

    //
    List<Map<String,Object>> findUserCount();
    
}
