package com.yjs.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yjs.constant.MessageConstant;
import com.yjs.constant.RedisConstant;
import com.yjs.entity.PageResult;
import com.yjs.entity.QueryPageBean;
import com.yjs.entity.Result;
import com.yjs.pojo.User;
import com.yjs.service.UserService;
import com.yjs.utlis.QiniuUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Reference
    private UserService userService;

    @Autowired
    private JedisPool jedisPool;
    
    
    //获取当前登录用户的用户名
    @RequestMapping("/getUsername")
    public Result getUsername()throws Exception{
        try{
            //从spring security框架中提供SecurityContextHolder来获取用户对象
            //1.SecurityContext 安全容器对象：先使用getContext()方法来获得安全容器对象
            //2.Authentication认证对象：再通过安全容器对象的getAuthentication()方法来获得认证对象
            //3.principal对象：最后通过认证对象的getPrincipal()方法获得principal对象，该对象可以强转为User对象
            org.springframework.security.core.userdetails.User user =
                    (org.springframework.security.core.userdetails.User)
                            SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        }catch (Exception e){
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }

    //新增用户组
    @RequestMapping("/add")
    public Result add(@RequestBody User user, Integer[] roleIds) {
        try {
            //调用service层的add方法，传入角色对象信息和所选角色的id
            userService.add(user, roleIds);
        } catch (Exception e) {
            //新增失败
            return new Result(false,"新增用户成功");
        }
        //新增成功
        return new Result(true, "新增用户失败");
    }

   

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {

        //调用业务层，传入页码，每页记录数，查询条件，得到分页结果集
        PageResult pageResult = userService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
        //返回结果集
        return pageResult;
    }

    //根据id查询角色的信息，用于编辑的回显
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        //调用service层方法，传入id，得到当前id的对象
        User user = userService.findById(id);
        //判断是否为空
        if (user != null) {
            //返回结果集
            return new Result(true,"查询用户成功", user);

        }
        //返回失败的结果集
        return new Result(false, "查询用户失败");
    }

    //根据用户id，查询对应所有角色的集合
    @RequestMapping("/findRoleIdsByUserId")
    public List<Integer> findRoleIdsByUserId(Integer id){
        List<Integer> list = userService.findRoleIdsByUserId(id);
        return list;
    }

    //编辑提交，根据数据修改角色的表
    @RequestMapping("/edit")
    public Result edit(@RequestBody User user,Integer[] roleIds){
        try {
            //调用service，传入新的角色对象，新的对应的检查项的id
            userService.edit(user,roleIds);
        }catch (Exception e){
            return new Result(false,"编辑用户失败");
        }
        
        return new Result(true,"编辑用户成功");
    }

    //根据id删除用户
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            //调用service，传入角色的id，根据id删除对应角色
            userService.delete(id);
        }catch (Exception e){
            return new Result(false,"删除用户失败");
        }
        return new Result(true,"删除用户成功");
    }
    
    
    
    
}
