package com.yjs.dao;

import com.github.pagehelper.Page;
import com.yjs.pojo.User;
import com.yjs.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    //根据用户名查询user对象表，返回user对象
    User findUserByUsername(String username);

    //添加用户
    void add(User user);

    void setUserAndRole(Map<String, Integer> map);

    //分页查询
    Page<User> selectByCondition(String queryString);

    //根据id，查询对应用户（回显）
    User findById(Integer id);

    //根据用户id，查询对应所有检查组的集合
    List<Integer> findRoleIdsByUserId(Integer id);

    //根据检查组id删除中间表数据（清理原有关联关系）
    void deleteAssociation(Integer id);

    //更新检查组基本信息
    void edit(User user);

    //根据id删除用户
    void deleteUserById(Integer userId);

    //获取所有用户信息（前端）
    List<User> findAll();

    List<Map<String,Object>> findUserCount();

    //根据用户名修改当前对象的密码
    void editPass(User user);
}
