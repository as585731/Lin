package com.yjs.dao;

import com.github.pagehelper.Page;
import com.yjs.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberDao {
    public List<Member> findAll();
    public Page<Member> selectByCondition(String queryString);
    public void add(Member member);
    public void deleteById(Integer id);
    public Member findById(Integer id);
    public Member findByTelephone(String telephone);
    public void edit(Member member);
    
    //根据日期统计会员数，统计指定日期之前的会员数
    public Integer findMemberCountBeforeDate(String date);
    
    public Integer findMemberCountByDate(String date);
    public Integer findMemberCountAfterDate(String date);
    public Integer findMemberTotalCount();

    //查询女会员数量
    Integer selectMemberGirl();
    
    //查询男会员数量
    Integer selectMemberMan();

    //查询0-18岁用户
    Integer selectMemberByAgeA();

    //查询18-30岁用户
    Integer selectMemberByAgeB();

    //查询30-45岁用户
    Integer selectMemberByAgeC();

    //查询45及岁以上用户
    Integer selectMemberByAgeD();

    //根据年龄扥段查询user表用户数量，返回一个list
    List<Map<String, Object>> querUserAge();
}
