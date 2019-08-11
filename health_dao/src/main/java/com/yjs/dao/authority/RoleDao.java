package com.yjs.dao.authority;

import com.github.pagehelper.Page;
import com.yjs.pojo.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RoleDao {
    //根据用户id进行多表关联角色表查询该用户所属所有角色，返回一个set集合
    Set<Role> findByUid(Integer uid);
    
    //新增角色
    void add(Role role);

    //设置角色合和权限表的关联关系（设置中间表）
    void setRoleAndPermission(Map<String, Integer> map);

    //设置角色合和菜单表的关联关系（设置中间表）
    void setRoleAndMenu(Map<String, Integer> map);
    

    //按条件查询角色的表
    Page<Role> selectByCondition(String queryString);

    //调用dao层根据id进行查询，返回对象
    Role findById(Integer id);

    //调用dao层根据角色id查询对应所有权限的id
    List<Integer> findPermissionIdsByRoleId(Integer id);

    //根据角色id删除中间表数据（清理原有关联关系）
    void deleteAssociation(Integer id);

    //根据角色id删除中间表数据（清理原有关联关系）
    void deleteAssociation2(Integer id);

    //更新角色基本信息
    void edit(Role role);

    //根据id删除对应角色的字段
    void deleteRoleById(Integer roleId);

    //查询所有角色
    List<Role> findAll();

    //调用dao层根据角色id查询对应所有菜单的id
    List<Integer> findMenuIdsByRoleId(Integer id);

    //查询该角色被用户表引用数量
    Integer selectConutUser(Integer RoleId);

    //根据id查询所有角色
//    List<Role> findRoleById(Integer id);

}
