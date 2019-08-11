package com.yjs.controller.authority;


import com.alibaba.dubbo.config.annotation.Reference;
import com.yjs.Exception.SubmenuException;
import com.yjs.constant.MessageConstant;
import com.yjs.entity.PageResult;
import com.yjs.entity.QueryPageBean;
import com.yjs.entity.Result;
import com.yjs.pojo.Menu;
import com.yjs.pojo.User;
import com.yjs.service.authority.MenuService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

//菜单管理
@RestController
@RequestMapping("/menu")
public class MenuController {
    
    @Reference
    private MenuService menuService;

    //新增
    @RequestMapping("/add.do")
    public Result add(@RequestBody Menu menu){
        try {
            menuService.add(menu);
        } catch (Exception e) {
            //报错，结果集设置为false，封装入提示信息
            e.printStackTrace();
            return new Result(false, "新增菜单失败");
        }
        //成功，结果集设置为true，封装入提示信息
        return new Result(true,"新增菜单成功");
    }

    @RequestMapping("/findPage")
    //分页查询
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        //调用service层的方法进行查询
        PageResult pageResult = menuService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        //响应结果集
        return pageResult;
    }

    //删除
    @RequestMapping("/delete")
    public Result delete(Integer id){
        //调用service层的方法进行删除处理
        try {
            menuService.delete(id);
        }catch (RuntimeException e){
            //如果运行报错，结果集设置false，传入抛出的提示信息（当前菜单被引用,还有子菜单未删除）
            return new Result(false,e.getMessage());
        }catch (Exception e){
            //其他错误设置
            return new Result(false, "删除菜单失败");
        }
        //成功，设置true，传入提示信息
        return new Result(true,"删除菜单成功");
    }

    //根据指定id查询检查菜单
    @RequestMapping("findById")
    public Result findById(Integer id){
        //调用service层，以传入的check的id查询出数据
        try {
            Menu menu = menuService.findById(id);
            //执行成功
            return new Result(true,"查询菜单成功",menu);
        }catch (Exception e){
            //执行失败
            return new Result(false,"查询菜单失败");
        }

    }

    //编辑
    @RequestMapping("/edit")
    public Result edit(@RequestBody Menu menu){
        //调用service层，以传入的check对象进行数据更新
        try {
            menuService.edit(menu);
        }catch (RuntimeException e){
            //如果运行报错，结果集设置false，传入抛出的提示信息（当前菜单被引用,还有子菜单未删除）
            return new Result(false,e.getMessage());
        }catch (Exception e){
            //其他错误
            return new Result(false,"编辑菜单失败");
        }
        return new Result(true,"编辑菜单成功");
    }

    //查询所有菜单
    @RequestMapping("/findAll")
    public Result findAll(){
        //调用业务层，查询出所有菜单的list集合
        List<Menu> menuList =menuService.findAll();
        //判断集合不为空或者长度大于0
        if(menuList != null && menuList.size() > 0){
            //查询成功，封装入结果集对象返回
            return new Result(true, "查询菜单成功",menuList);
        }
        //如果为空或者长度为0，代表查询失败
        return new Result(false,"查询菜单 ");

    }

    //获取当前登录用户的用户名和密码，得到该用户的菜单对象
    @RequestMapping("/getMenu")
    public Result getUsername()throws Exception{
        try{
            org.springframework.security.core.userdetails.User user =
                    (org.springframework.security.core.userdetails.User)
                            SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //获得用户名
            String username = user.getUsername();
            //调用service层方法得到该用户的菜单集合
            List<Menu> menus = menuService.selectByUser(username);
            //返回结果集
            return new Result(true,MessageConstant.GET_MENU_SUCCESS,menus);

        }catch (Exception e){
            return new Result(false, MessageConstant.GET_MENU_FAIL);
        }
    }

    
    //查询所有level为1的菜单的id
    @RequestMapping("/selectParentMenuIds")
    public Result selectParentMenuIds() {
        List<Map> ParentMenuIds = null;
        try {
            ParentMenuIds = menuService.selectParentMenuIds();
            //返回结果集
            return new Result(true, "查询一级菜单成功", ParentMenuIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "查询一级菜单失败", ParentMenuIds);
        }
    }
    
}
