package com.yjs.service.authority;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yjs.dao.authority.RoleDao;
import com.yjs.entity.PageResult;
import com.yjs.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//角色服务
@Service(interfaceClass = RoleService.class)
public class RoleServiceImp implements RoleService{

    @Autowired
    RoleDao roleDao;

    @Override
    //新增角色
    public void add(Role role, Integer[] permissionIds,Integer[] menuIds) {
        //调用dao层的方法新增角色
        roleDao.add(role);
        //设置角色和权限和菜单的关联关系（设置中间表）
        setRoleAndPermission(role.getId(),permissionIds,menuIds);
    }

    @Override
    //分页查询
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        //使用插件进行分页查询
        PageHelper.startPage(currentPage,pageSize);
        //传入条件进行查询
        Page<Role> page =  roleDao.selectByCondition(queryString);
        //从page对象中取出我们要的数据，封装入PageResult对象返回
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    //根据id查询角色的信息，用于编辑的回显
    public Role findById(Integer id) {
        //调用dao层根据id进行查询，返回对象
        return roleDao.findById(id);
    }

    @Override
    //根据角色合id查询对应的所有权限的id
    public List<Integer> findPermissionIdsByRoleId(Integer id) {
        //调用dao层根据角色id查询对应所有权限的id
        return roleDao.findPermissionIdsByRoleId(id);
    }

    //根据角色合id查询对应的所有菜单的id
    public List<Integer> findMenuIdsByRoleId(Integer id) {
        //调用dao层根据角色id查询对应所有权限的id
        return roleDao.findMenuIdsByRoleId(id);
    }

    @Override
    //编辑提交，根据数据修改角色的表
    public void edit(Role role, Integer[] permissionIds,Integer[] menuIds) {
        //根据角色id删除权限中间表数据（清理原有关联关系）
        roleDao.deleteAssociation(role.getId());
        //根据角色id删除权限中间表数据（清理原有关联关系）
        roleDao.deleteAssociation2(role.getId());
        //向中间表(t_role_permission)插入数据（建立角色和权限关联关系）
        setRoleAndPermission(role.getId(),permissionIds,menuIds);
        //更新角色基本信息
        roleDao.edit(role);

    }

    @Override
    //根据id删除角色
    public void delete(Integer roleId) {
        //查询该角色是被引用数量
        Integer num = roleDao.selectConutUser(roleId);
        //如果有被引用，则抛出异常
        if (num>0) {
            throw new RuntimeException("该角色被用户引用，删除失败");
        }
        
        //根据角色id删除中间表数据（清理原有关联关系）
        roleDao.deleteAssociation(roleId);
        roleDao.deleteAssociation2(roleId);
        //根据角色id删除角色表的对应字段
        roleDao.deleteRoleById(roleId);
    }

    @Override
    //查询所有角色
    public List<Role> findAll() {
        return roleDao.findAll();
    }


    //设置角色合和权限表及菜单表的关联关系
    public void setRoleAndPermission(Integer roleId,Integer[] permissionIds,Integer[] menuIds){
        if(permissionIds != null && permissionIds.length > 0){
            for (Integer permissionId : permissionIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("role_id",roleId);
                map.put("permission_id",permissionId);
                roleDao.setRoleAndPermission(map);
            }
        }

        if(menuIds != null && menuIds.length > 0){
            for (Integer menuId : menuIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("role_id",roleId);
                map.put("menu_id",menuId);
                try {
                    roleDao.setRoleAndMenu(map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        
    }
}
