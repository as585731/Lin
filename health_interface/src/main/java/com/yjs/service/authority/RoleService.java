package com.yjs.service.authority;

import com.yjs.entity.PageResult;
import com.yjs.pojo.Role;

import java.util.List;

public interface RoleService {
    //新增角色
    void add(Role role, Integer[] permissionIds,Integer[] menuIds);
    //分页查询
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    //根据id查询角色的信息，用于编辑的回显
    Role findById(Integer id);

    //根据角色合id查询对应的所有检查项的id
    List<Integer> findPermissionIdsByRoleId(Integer id);

    //根据角色合id查询对应的所有检查项的id
    List<Integer> findMenuIdsByRoleId(Integer id);

    //编辑提交，根据数据修改角色的表
    void edit(Role role, Integer[] permissionIds,Integer[] menuIds);

    //根据id删除角色
    void delete(Integer roleId);

    //查询所有角色
    List<Role> findAll();

}
