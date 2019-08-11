package com.yjs.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import com.yjs.constant.MessageConstant;
import com.yjs.entity.QueryPageBean;
import com.yjs.entity.Result;
import com.yjs.service.MemberService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {
    
    @Reference
    private MemberService memberService;

    // 新增用户
    @RequestMapping("/addMember")
    public Result addMember(@RequestBody com.yjs.pojo.Member member) {
        try {
            memberService.add(member);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_MEMBER_FAIL);
        }
        return new Result(true, MessageConstant.ADD_MEMBER_SUCCESS);
    }

    // 分页
    @RequestMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        try {
            Result members = memberService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
            return members;
        } catch (Exception e) {
            return new Result(false,MessageConstant.QUERY_MEMBER_FAIL);
        }
    }

    // 编辑的数据回显，根据用户id查询一条用户信息
    @RequestMapping("/findMemberByMemberId")
    public Result findMemberByMemberId(Integer id) {
        try {
            com.yjs.pojo.Member member = memberService.findById(id);
            return new Result(true, MessageConstant.QUERY_MEMBER_SUCCESS,member);
        } catch (Exception e) {
            return new Result(false, MessageConstant.QUERY_MEMBER_FAIL);
        }
    }


    // 编辑用户
    @RequestMapping("/editMember")
    public Result editMember(@RequestBody com.yjs.pojo.Member member ) {
        try {
            memberService.edit(member);
        } catch (Exception e) {
            return new Result(false, MessageConstant.EDIT_MEMBER_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_MEMBER_SUCCESS);
    }

    //删除用户
    @RequestMapping("/deleteMemberByMemberId")
    public Result deleteMemberByMemberId(Integer id){
        try {
            memberService.deleteById(id);
            return new Result(true,MessageConstant.DELETE_MEMBER_SUCCESS);
        } catch (Exception e) {
            return new Result(false,MessageConstant.DELETE_MEMBER_FAIL);
        }
    }

}
