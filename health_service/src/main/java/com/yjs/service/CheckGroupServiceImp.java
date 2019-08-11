package com.yjs.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yjs.dao.CheckGroupDao;
import com.yjs.entity.PageResult;
import com.yjs.pojo.CheckGroup;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//检查组服务
@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImp implements CheckGroupService{

    @Autowired
    CheckGroupDao checkGroupDao;

    @Override
    //新增检查组
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //调用dao层的方法新增检查组
        checkGroupDao.add(checkGroup);
        //设置检查组和检查项的关联关系（设置中间表）
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
    }

    @Override
    //分页查询
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        //使用插件进行分页查询
        PageHelper.startPage(currentPage,pageSize);
        //传入条件进行查询
        Page<CheckGroup> page =  checkGroupDao.selectByCondition(queryString);
        //从page对象中取出我们要的数据，封装入PageResult对象返回
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    //根据id查询检查组的信息，用于编辑的回显
    public CheckGroup findById(Integer id) {
        //调用dao层根据id进行查询，返回对象
        return checkGroupDao.findById(id);
    }

    @Override
    //根据检查组合id查询对应的所有检查项的id
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        //调用dao层根据检查组id查询对应所有检查项的id
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    @Override
    //编辑提交，根据数据修改检查组的表
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //根据检查组id删除中间表数据（清理原有关联关系）
        checkGroupDao.deleteAssociation(checkGroup.getId());
        //向中间表(t_checkgroup_checkitem)插入数据（建立检查组和检查项关联关系）
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
        //更新检查组基本信息
        checkGroupDao.edit(checkGroup);

    }

    @Override
    //根据id删除检查组
    public void delete(Integer CheckGroupId) {
        //根据检查组id删除中间表数据（清理原有关联关系）
        checkGroupDao.deleteAssociation(CheckGroupId);
        //根据检查组id删除检查组表的对应字段
        checkGroupDao.deleteCheckGroupById(CheckGroupId);
    }

    @Override
    //查询所有检查组
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }


    //设置检查组合和检查项的关联关系
    public void setCheckGroupAndCheckItem(Integer checkGroupId,Integer[] checkitemIds){
        if(checkitemIds != null && checkitemIds.length > 0){
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("checkgroup_id",checkGroupId);
                map.put("checkitem_id",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }
}
