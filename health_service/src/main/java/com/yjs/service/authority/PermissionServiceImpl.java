package com.yjs.service.authority;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yjs.dao.authority.PermissionDao;
import com.yjs.entity.PageResult;
import com.yjs.pojo.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;
    
    @Override
    //根据表单提交的数据新增权限
    @Transactional
    public void add(Permission permission) {
        permissionDao.add(permission);
    }

    @Override
    //进行分页查询
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        //基于Mybatis分页助手插件实现分页
        PageHelper.startPage(currentPage,pageSize);
        Page<Permission> page = permissionDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    //根据id删除指定权限
    public void delete(Integer id) {
        //查询当前权限是否和检查组关联
        long count = permissionDao.findCountByPermissionId(id);
        if(count > 0){
            //当前权限被引用，不能删除，抛出异常并设置提示信息，供controller层捕获
            throw new RuntimeException("当前权限被引用，不能删除");
        }
        //根据id删除指定权限
        permissionDao.deleteById(id);
    }

    @Override
    //以传入的check对象进行数据更新
    public void edit(Permission permission) {
        //调用dao层进行更新数据
        permissionDao.edit(permission);
    }

    @Override
    //根据指定id查询检查权限
    public Permission findById(Integer id) {
        return permissionDao.findById(id);
    }

    @Override
    //查询所有权限
    public List<Permission> findAll() {
        return permissionDao.findAll();
    }
}
