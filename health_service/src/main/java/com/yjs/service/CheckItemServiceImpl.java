package com.yjs.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yjs.dao.CheckItemDao;
import com.yjs.entity.PageResult;
import com.yjs.pojo.CheckItem;
import com.yjs.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService{

    @Autowired
    private CheckItemDao checkItemDao;
    @Override
    //根据表单提交的数据新增检查项
    @Transactional
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    //进行分页查询
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        //基于Mybatis分页助手插件实现分页
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    //根据id删除指定检查项
    public void delete(Integer id) {
        //查询当前检查项是否和检查组关联
        long count = checkItemDao.findCountByCheckItemId(id);
        if(count > 0){
            //当前检查项被引用，不能删除，抛出异常并设置提示信息，供controller层捕获
            throw new RuntimeException("当前检查项被引用，不能删除");
        }
        //根据id删除指定检查项
        checkItemDao.deleteById(id);
    }

    @Override
    //以传入的check对象进行数据更新
    public void edit(CheckItem checkItem) {
        //调用dao层进行更新数据
        checkItemDao.edit(checkItem);
    }

    @Override
    //根据指定id查询检查检查项
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    @Override
    //查询所有检查项
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
