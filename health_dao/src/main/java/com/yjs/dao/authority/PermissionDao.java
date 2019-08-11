package com.yjs.dao.authority;

import com.github.pagehelper.Page;
import com.yjs.pojo.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionDao {
    //根据角色id进行多表关联查询，查询该角色所拥有的所有权限对象，返回一个集合
    Set<Permission> findByRoleId(Integer roleId);

    //根据表单提交的数据新增检查项
    void add(Permission permission);

    //进行分页查询
    Page<Permission> selectByCondition(String queryString);

    //查询当前检查项是否和检查组关联
    long findCountByPermissionId(Integer id);

    //根据id删除指定检查项
    void deleteById(Integer id);

    //以传入的check对象进行数据更新
    void edit(Permission permission);

    //根据指定id查询检查检查项
    Permission findById(Integer id);

    //查询所有检查项
    List<Permission> findAll();

    //根据检查组id查询检查项信息
//    List<Permission>findPermissionById(Integer id);
    
}
