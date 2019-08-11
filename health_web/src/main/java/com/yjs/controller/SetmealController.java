package com.yjs.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yjs.constant.MessageConstant;
import com.yjs.constant.RedisConstant;
import com.yjs.entity.PageResult;
import com.yjs.entity.QueryPageBean;
import com.yjs.entity.Result;
import com.yjs.pojo.CheckGroup;
import com.yjs.pojo.Setmeal;
import com.yjs.service.CheckItemService;
import com.yjs.service.SetmealService;
import com.yjs.utlis.QiniuUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

//套餐项管理
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    //新增套餐组
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkGroupIds) {
        try {
            //调用service层的add方法，传入检查组对象信息和所选检查组的id
            setmealService.add(setmeal, checkGroupIds);
        } catch (Exception e) {
            //新增失败
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        //新增成功
        //将文件名存到redis中
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    //文件上传到七牛云
    @RequestMapping("/upload")
    public Result add(@RequestParam MultipartFile imgFile) {
        String newName=null;
        try {
            // 获取文件原始名称
            String filename = imgFile.getOriginalFilename();
            // 获取后缀名
            String extName = FilenameUtils.getExtension(filename);
            // 新名称，使用JDK的UUID工具类生成一个随机码作为文件名
            newName = UUID.randomUUID().toString().replace("-", "") + "." + extName;
            //调用工具类上传到七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),newName);
            //将文件名存到redis中
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,newName);
        } catch (Exception e) {
            //新增失败
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        //新增成功
        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,newName);
    }

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {

        //调用业务层，传入页码，每页记录数，查询条件，得到分页结果集
        PageResult pageResult = setmealService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
        //返回结果集
        return pageResult;
    }

    //根据id查询检查组的信息，用于编辑的回显
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        //调用service层方法，传入id，得到当前id的对象
        Setmeal setmeal = setmealService.findById(id);
        //判断是否为空
        if (setmeal != null) {
            //返回结果集
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);

        }
        //返回失败的结果集
        return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
    }

    //根据套餐id，查询对应所有检查组的集合
    @RequestMapping("/findCheckGroupIdsBySetmealId")
    public List<Integer> findCheckGroupIdsBySetmealId(Integer id){
        List<Integer> list = setmealService.findCheckGroupIdsBySetmealId(id);
        return list;
    }

    //编辑提交，根据数据修改检查组的表
    @RequestMapping("/edit")
    public Result edit(@RequestBody Setmeal setmeal,Integer[] checkGroupIds){
        //获得原来的img名称
        String img= setmealService.findById(setmeal.getId()).getImg();
        try {
            //调用service，传入新的检查组对象，新的对应的检查项的id
            setmealService.edit(setmeal,checkGroupIds);
        }catch (Exception e){
            return new Result(false,MessageConstant.EDIT_SETMEAL_FAIL);
        }
        //将文件名存到redis中
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        //把旧的名字从redis删除
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,img);
        return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS);
    }

    //根据id删除套餐
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            //调用service，传入检查组的id，根据id删除对应检查组
            setmealService.delete(id);
        }catch (Exception e){
            return new Result(false,MessageConstant.DELETE_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_SETMEAL_SUCCESS);
    }


    
}
