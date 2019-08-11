package com.yjs.controller.authority;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yjs.constant.MessageConstant;
import com.yjs.entity.PageResult;
import com.yjs.entity.QueryPageBean;
import com.yjs.entity.Result;
import com.yjs.pojo.Role;
import com.yjs.service.authority.RoleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//角色管理
@RestController
@RequestMapping("/role")
public class RoleController {
    @Reference
    private RoleService roleService;

    //新增角色
    @RequestMapping("/add")
    public Result add(@RequestBody Role role, Integer[] permissionIds,Integer[] menuIds) {
        try {
            //调用service层的add方法，传入角色对象信息和所选检查项的id
            roleService.add(role, permissionIds,menuIds);
        } catch (Exception e) {
            //新增失败
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        //新增成功
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
//       if(!(queryPageBean.getQueryString().equals(""))){
//           queryPageBean.setCurrentPage(1);
//       }
        //调用业务层，传入页码，每页记录数，查询条件，得到分页结果集
        PageResult pageResult = roleService.pageQuery(
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
        Role role = roleService.findById(id);
        //判断是否为空
        if (role != null) {
            //返回结果集
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, role);

        }
        //返回失败的结果集
        return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

    //根据角色合id查询对应的所有权限的id
    @RequestMapping("/findPermissionIdsByRoleId")
    public List<Integer> findPermissionIdsByRoleId(Integer id){
        List<Integer> list = roleService.findPermissionIdsByRoleId(id);
        return list;
    }

    //根据角色合id查询对应的所有菜单的id
    @RequestMapping("/findMenuIdsByRoleId")
    public List<Integer> findMenuIdsByRoleId(Integer id){
        List<Integer> list = roleService.findMenuIdsByRoleId(id);
        return list;
    }

    //编辑提交，根据数据修改角色的表
    @RequestMapping("/edit")
    public Result edit(@RequestBody Role role,Integer[] permissionIds,Integer[] menuIds){
        try {
            //调用service，传入新的角色对象，新的对应的检查项的id
            roleService.edit(role,permissionIds,menuIds);
        }catch (Exception e){
            return new Result(false,"编辑角色失败");
        }
        return new Result(true,"编辑角色成功");
    }

    //根据id删除角色项
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            //调用service，传入角色的id，根据id删除对应角色
            roleService.delete(id);
        }catch (RuntimeException e){
            //该角色被引用
            return new Result(false,e.getMessage());
        }
        catch (Exception e){
            return new Result(false,"删除角色失败");
        }
        return new Result(true,"删除角色成功");
    }

    //查询所有角色
    @RequestMapping("/findAll")
    public Result findAll(){
        //调用业务层，查询出所有检查项的list集合
        List<Role> roleList =roleService.findAll();
        //判断集合不为空或者长度大于0
        if(roleList != null && roleList.size() > 0){
            //查询成功，封装入结果集对象返回
            return new Result(true, "查询角色成功",roleList);
        }
        //如果为空或者长度为0，代表查询失败
        return new Result(false,"查询角色失败");

    }
}
