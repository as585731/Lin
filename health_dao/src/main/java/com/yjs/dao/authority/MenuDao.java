package com.yjs.dao.authority;

import com.github.pagehelper.Page;
import com.yjs.pojo.Menu;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MenuDao {
    //根据用户对象，查询该用户所属的菜单集合
    List<Menu> selectByUser(String username);
    
    //根据角色id进行多表关联查询，查询该角色所拥有的所有菜单对象，返回一个集合
    Set<Menu> findByRoleId(Integer roleId);

    //根据表单提交的数据新增菜单
    void add(Menu menu);

    //进行分页查询
    Page<Menu> selectByCondition(String queryString);

    //查询当前菜单是否和角色关联
    long findCountByMenuId(Integer id);

    //根据id删除指定菜单
    void deleteById(Integer id);

    //以传入的Menu对象进行数据更新
    void edit(Menu menu);

    //根据指定id查询检查菜单
    Menu findById(Integer id);

    //查询所有菜单
    List<Menu> findAll();

    //查询所有level为1的菜单的id
    List<Map> selectParentMenuIds();

    //查询该菜单的子菜单数量
    Integer selectSubmenuCount(Integer id);

    //根据角色id查询菜单信息
//    List<Menu>findMenuById(Integer id);
    
}
