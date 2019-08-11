package com.yjs.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yjs.constant.RedisConstant;
import com.yjs.dao.CheckItemDao;
import com.yjs.dao.SetmealDao;
import com.yjs.entity.PageResult;
import com.yjs.pojo.CheckGroup;
import com.yjs.pojo.CheckItem;
import com.yjs.pojo.Setmeal;
import com.yjs.service.CheckItemService;
import com.yjs.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//套餐
@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService{

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;


    @Override
    //新增套餐组
    public void add(Setmeal setmeal, Integer[] checkGroupIds) {
        //调用dao层的方法新增检查组
        setmealDao.add(setmeal);
        //设置检查组和检查项的关联关系（设置中间表）
        setSetmealAndCheckGroup(setmeal.getId(),checkGroupIds);

        //清除redis缓存
        cleanRedis(null);
    }

    @Override
    //分页查询
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        //使用插件进行分页查询
        PageHelper.startPage(currentPage,pageSize);
        //传入条件进行查询
        Page<Setmeal> page =  setmealDao.selectByCondition(queryString);
        //从page对象中取出我们要的数据，封装入PageResult对象返回
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    //根据id，查询对应套餐（回显）
    public Setmeal findById(Integer id) {
        Setmeal setmeal = null;
        //判断redis内是否有所有套餐的数据
        //拿到jedis链接
        Jedis jedis = jedisPool.getResource();
        //从redis数据中查找数据
        String reCate = jedis.get("SetmealBy"+id);
        //得到一个jackson对象
        ObjectMapper om = new ObjectMapper();
        try {
            //判断该数据是否为空
            if (reCate == null) {
                //如果为空，代表Redis数据库中还没有数据，从Mysql中查找
                setmeal = setmealDao.findById(id);
                //将该对象转为josn类型，存到redis数据库
                String json = om.writeValueAsString(setmeal);
                //储存到redis数据库
                jedis.set("SetmealBy"+id,json);
            }else {
                //如果不为null，则将查找到的数据转为Setmeal对象
                setmeal = om.readValue(reCate,Setmeal.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //归还链接
            jedis.close();
        }
        //返回该对象
        return setmeal;
    }

    @Override
    //根据套餐id，查询对应所有检查组的集合
    public List<Integer> findCheckGroupIdsBySetmealId(Integer id) {
        //调用dao层根据检查组id查询对应所有检查项的id
        return setmealDao.findCheckGroupIdsBySetmealId(id);
    }

    @Override
    //编辑提交，根据数据修改检查组的表
    public void edit(Setmeal setmeal, Integer[] checkGroupIds) {
        //根据检查组id删除中间表数据（清理原有关联关系）
        setmealDao.deleteAssociation(setmeal.getId());
        //向中间表插入数据（建立检查组和检查项关联关系）
        setSetmealAndCheckGroup(setmeal.getId(),checkGroupIds);
        //更新检查组基本信息
        setmealDao.edit(setmeal);
    }

    @Override
    //根据id删除套餐
    public void delete(Integer setmealId) {
        //根据检查组id删除中间表数据（清理原有关联关系）
        setmealDao.deleteAssociation(setmealId);
        //根据检查组id删除检查组表的对应字段
        setmealDao.deleteSetmealById(setmealId);
        //清除redis缓存
        cleanRedis(null);
        
        
    }

    @Override
    //获取所有套餐信息（前端）
    public List<Setmeal> findAll() {
        List<Setmeal> setmeals = null;
        //判断redis内是否有所有套餐的数据
        //拿到jedis链接
        Jedis jedis = jedisPool.getResource();
        //从redis数据中查找数据
        String reCate = jedis.get("Setmeal");
        //得到一个jackson对象
        ObjectMapper om = new ObjectMapper();
        try {
            //判断该数据是否为空
            if (reCate == null) {
                //如果为空，代表Redis数据库中还没有数据，从Mysql中查找
                setmeals = setmealDao.findAll();
                //将该list转为josn类型，村粗到redis数据库
                String json = om.writeValueAsString(setmeals);
                //储存到redis数据库
                jedis.set("Setmeal",json);
            }else {
                //如果不为课呢个，则将查找到的数据转为list集合
                setmeals = om.readValue(reCate, new TypeReference<List<Setmeal>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //归还链接
            jedis.close();
        }
        
        //返回
        return setmeals;
        
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }


    //设置套餐组合和检查组的关联关系【修改套餐组】
    public void setSetmealAndCheckGroup(Integer setmealId,Integer[] checkGroupIds){
        //非空判断
        if(checkGroupIds != null && checkGroupIds.length > 0){
            //遍历拿到所有检查组的id
            for (Integer checkGroupId : checkGroupIds) {
                Map<String,Integer> map = new HashMap<>();
                //把套餐id和检查组id存入map集合
                map.put("setmeal_id",setmealId);
                map.put("checkgroup_id",checkGroupId);
                //根据map内的id，修改套餐
                setmealDao.setSetmealAndCheckGroup(map);
                //清除redis缓存
                cleanRedis(setmealId);
            }
        }
    }
    
    //清除redis内套餐的缓存数据
    private void cleanRedis(Integer id){
        //清空redis内套餐的缓存数据
        Jedis jedis = jedisPool.getResource();
        //从redis数据中删除缓存
        jedis.del("Setmeal");
        
        if (id!=null){
            //判断id是否有传入id,如果有传入，根据id删除对应检查组的缓存
            jedis.del("SetmealBy"+id);
        }
        //释放资源
        jedis.close();
    }

}
