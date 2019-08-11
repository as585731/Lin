package com.yjs.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yjs.dao.UserDao;
import com.yjs.dao.authority.PermissionDao;
import com.yjs.dao.UserDao;
import com.yjs.dao.authority.RoleDao;
import com.yjs.entity.PageResult;
import com.yjs.pojo.Permission;
import com.yjs.pojo.Role;
import com.yjs.pojo.User;
import com.yjs.pojo.User;
import com.yjs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService{
    
    //注入几个dao对象
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    
    //根据用户名查询User
    @Override
    public User findUserByUsername(String username) {
        //1、根据用户查询user
        User user = userDao.findUserByUsername(username);
        
        //非空判断
        if (user!=null) {
            //2、根据用户的id查询角色
           Set<Role> roles = roleDao.findByUid(user.getId());

           //3、根据角色id查询角色的权限对象
            if (roles !=null&&roles.size()>0) {
                //循环遍历出每个角色对象
                for (Role role : roles) {
                    //拿到角色的id
                    Integer roleId = role.getId();
                    //查询出该角色的所属权限对象
                   Set<Permission> permissions =  permissionDao.findByRoleId(roleId);
                   //将该权限对象集合，设置进角色对象内
                    role.setPermissions(permissions);
                }
                //将该角色对象集合设置进user对象
                user.setRoles(roles);
            }
        }
       //返回user对象
        return user;
    }

    


    @Override
    //新增用户组
    public void add(User user, Integer[] roleIds) {
        //调用dao层的方法新增角色
        userDao.add(user);
        //设置角色和检查项的关联关系（设置中间表）
        setUserAndRole(user.getId(),roleIds);
    }

    @Override
    //分页查询
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        //使用插件进行分页查询
        PageHelper.startPage(currentPage,pageSize);
        //传入条件进行查询
        Page<User> page =  userDao.selectByCondition(queryString);
        //从page对象中取出我们要的数据，封装入PageResult对象返回
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    //根据id，查询对应用户（回显）
    public User findById(Integer id) {
        //返回该对象
        User user = userDao.findById(id);
        return user;
    }

    @Override
    //根据用户id，查询对应所有角色的集合
    public List<Integer> findRoleIdsByUserId(Integer id) {
        //调用dao层根据角色id查询对应所有检查项的id
        return userDao.findRoleIdsByUserId(id);
    }

    @Override
    //编辑提交，根据数据修改角色的表
    public void edit(User user, Integer[] roleIds) {
        //根据角色id删除中间表数据（清理原有关联关系）
        userDao.deleteAssociation(user.getId());
        //向中间表插入数据（建立角色和检查项关联关系）
        setUserAndRole(user.getId(),roleIds);
        //更新角色基本信息
        userDao.edit(user);
    }

    @Override
    //根据id删除用户
    public void delete(Integer userId) {
        //根据角色id删除中间表数据（清理原有关联关系）
        userDao.deleteAssociation(userId);
        //根据角色id删除角色表的对应字段
        userDao.deleteUserById(userId);

    }

    @Override
    //获取所有用户信息（前端）
    public List<User> findAll() {
        List<User> users = userDao.findAll();
        return users;

    }

    @Override
    public List<Map<String, Object>> findUserCount() {
        return userDao.findUserCount();
    }


    //设置用户组合和角色的关联关系【修改用户组】
    public void setUserAndRole(Integer userId,Integer[] roleIds){
        //非空判断
        if(roleIds != null && roleIds.length > 0){
            //遍历拿到所有角色的id
            for (Integer roleId : roleIds) {
                Map<String,Integer> map = new HashMap<>();
                //把用户id和角色id存入map集合
                map.put("user_id",userId);
                map.put("role_id",roleId);
                //根据map内的id，修改用户
                userDao.setUserAndRole(map);
            }
        }
    }
    

}
