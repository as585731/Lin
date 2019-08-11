package com.yjs.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yjs.constant.MessageConstant;
import com.yjs.entity.PageResult;
import com.yjs.entity.QueryPageBean;
import com.yjs.entity.Result;
import com.yjs.pojo.CheckGroup;
import com.yjs.pojo.CheckItem;
import com.yjs.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//检查组管理
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;

    //新增检查组
    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        try {
            //调用service层的add方法，传入检查组对象信息和所选检查项的id
            checkGroupService.add(checkGroup, checkitemIds);
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
        PageResult pageResult = checkGroupService.pageQuery(
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
        CheckGroup checkGroup = checkGroupService.findById(id);
        //判断是否为空
        if (checkGroup != null) {
            //返回结果集
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroup);

        }
        //返回失败的结果集
        return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

    //根据检查组合id查询对应的所有检查项的id
    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id){
        List<Integer> list = checkGroupService.findCheckItemIdsByCheckGroupId(id);
        return list;
    }

    //编辑提交，根据数据修改检查组的表
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try {
            //调用service，传入新的检查组对象，新的对应的检查项的id
            checkGroupService.edit(checkGroup,checkitemIds);
        }catch (Exception e){
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    //根据id删除检查组项
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            //调用service，传入检查组的id，根据id删除对应检查组
            checkGroupService.delete(id);
        }catch (Exception e){
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    //查询所有检查组
    @RequestMapping("/findAll")
    public Result findAll(){
        //调用业务层，查询出所有检查项的list集合
        List<CheckGroup> checkGroupList =checkGroupService.findAll();
        //判断集合不为空或者长度大于0
        if(checkGroupList != null && checkGroupList.size() > 0){
            //查询成功，封装入结果集对象返回
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupList);
        }
        //如果为空或者长度为0，代表查询失败
        return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);

    }
}
