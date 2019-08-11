package com.yjs.service;

import com.yjs.pojo.Member;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface MemberService {
    //添加会员
    void add(Member member);

    //根据电话号码，查询对应会员对象
    Member findByTelephone(String telephone);

    //根据list集合内的月份，统计每个月的会员数量
    Map<String,Object> findMemberCountByMonth(String value1) throws ParseException;


    //查询男女用户的数量，男女各一个map，名字的key为name，数量的key为value
    List<Map<String,Object>> selectMemberSex();

    //查询各年龄段用户数量，年龄段名字的key为name，数量的key为value
    List<Map<String,Object>> selectMemberAge();
}
