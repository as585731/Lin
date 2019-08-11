package com.yjs.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yjs.constant.MessageConstant;
import com.yjs.dao.MemberDao;
import com.yjs.entity.PageResult;
import com.yjs.entity.Result;
import com.yjs.pojo.Member;
import com.yjs.service.MemberService;
import com.yjs.utlis.DateUtils;
import com.yjs.utlis.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = MemberService.class)
public class MemberServiceImp implements MemberService {

    @Autowired
    MemberDao memberDao;

    @Override
    //添加会员
    public void add(Member member) {
        //判断密码是否为空
        if (member.getPassword() != null) {
            //不为空给密码加密
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }

    @Override
    //根据电话号码，查询对应会员对象
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }


    //根据list集合内的月份，统计每个月的会员数量
    public Map<String, Object> findMemberCountByMonth(String value1) throws ParseException {
        //创建一个集合，用来存储需要查询的月份
        List<String> month = new ArrayList<>();

        //入参非空判断
        if (value1 == null || value1.equals("")) {
            //创建一个日历对象
            Calendar calendar = Calendar.getInstance();
            //将日历对象的月份字段，设置为12个月前
            calendar.add(Calendar.MONTH, -12);

            for (int i = 0; i < 12; i++) {
                //遍历12次，每次将日历对象内的月份+1
                calendar.add(Calendar.MONTH, 1);
                //将每个月的yyyy.MM形式的时间存入list集合
                month.add(new SimpleDateFormat("yyyy.MM").format(calendar.getTime()));
            }
        } else {

            //代表有参数
            String[] split = value1.split(",");
            //创建一个dateFormt对象，将字符串和日期进行转换
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            //开始的日期
            Date startDate = sdf.parse(split[0]);
            //结束的日期
            Date endDate = sdf.parse(split[1]);

            //创建一个日历对象
            Calendar calendar = Calendar.getInstance();
            //将开始时间设置为日历对象
            calendar.setTime(startDate);
            //判断日历的对象时间是否小于等于结束的时间
            while (calendar.getTime().compareTo(endDate) < 1) {
                //如果小于等于则月份存入集合
                month.add(sdf.format(calendar.getTime()));
                System.out.println(sdf.format(calendar.getTime()));
                //月份+1
                calendar.add(Calendar.MONTH, 1);
            }


        }


        //将list集合存入map集合，key为months
        Map<String, Object> map = new HashMap<>();
        map.put("months", month);

        List<Integer> memberCount = new ArrayList<>();
        //遍历list集合
        for (String m : month) {
            //字符串拼接每个最后一天
            m = m + ".31";//格式：2019.04.31
            //查询
            Integer count = memberDao.findMemberCountBeforeDate(m);
            memberCount.add(count);
        }
        map.put("memberCount", memberCount);

        return map;

    }

    @Override
    //查询男女用户的数量，男女各一个map，名字的key为name，数量的key为value
    public List<Map<String, Object>> selectMemberSex() {
        //查询女用户的数量，并存入map集合
        Integer girlNum = memberDao.selectMemberGirl();
        Map<String, Object> girlMap = new HashMap<>();
        girlMap.put("value", girlNum);
        girlMap.put("name", "女");

        //查询男用户的数量，并存入map集合
        Integer manNum = memberDao.selectMemberMan();
        Map<String, Object> manMap = new HashMap<>();
        manMap.put("value", manNum);
        manMap.put("name", "男");

        //存入List集合返回
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(girlMap);
        list.add(manMap);
        return list;

    }

    @Override
    //查询各年龄段用户数量，年龄段名字的key为name，数量的key为value
    public List<Map<String, Object>> selectMemberAge() {

        /*List<Map<String, Object>> list = new ArrayList<>();
        //查询0-18岁用户
        Integer ageByNumA = memberDao.selectMemberByAgeA();
        if (ageByNumA > 0) {
            Map<String, Object> ageMapA = new HashMap<>();
            ageMapA.put("value", ageByNumA);
            ageMapA.put("name", "0-18岁");
            list.add(ageMapA);
        }

        //查询18-30岁用户
        Integer ageByNumB = memberDao.selectMemberByAgeB();
        if (ageByNumB > 0) {
            Map<String, Object> ageMapB = new HashMap<>();
            ageMapB.put("value", ageByNumB);
            ageMapB.put("name", "18-30岁");
            list.add(ageMapB);
        }
        
        //查询30-45岁用户
        Integer ageByNumC = memberDao.selectMemberByAgeC();
        if (ageByNumC > 0) {
            Map<String, Object> ageMapC = new HashMap<>();
            ageMapC.put("value", ageByNumC);
            ageMapC.put("name", "30-45岁");
            list.add(ageMapC);
        }
        
        //查询45及岁以上用户
        Integer ageByNumD = memberDao.selectMemberByAgeD();
        if (ageByNumD > 0) {
            Map<String, Object> ageMapD = new HashMap<>();
            ageMapD.put("value", ageByNumD);
            ageMapD.put("name", "45岁以上");
            list.add(ageMapD);
        }*/
        //返回list集合
        return memberDao.querUserAge();  
    }


    // 根据月份动态获取会员数量
    @Override
    public Map<String, Object> countUserDynamic(String startDate, String endDate) throws Exception {
        // 创建map存放months和memberCount
        Map<String, Object> map = new HashMap<>();
        //创建list存放months
        List<String> list = new ArrayList<>();
        //创建list存放memberCount
        List<Integer> memberCount = new ArrayList<>();
        // 获取日历实例对象
        Calendar calendar = Calendar.getInstance();

        // 将String日期转为date类型
        Date newStartDate = DateUtils.parseString2Date(startDate);

        // 将日历时间设置为页面传过来的起始时间
        calendar.setTime(newStartDate);

        // 计算两个日期之间的月份差，用作循环条件
        int monthCount = getMonthSpace(startDate, endDate);
        for (int i = 0; i <= monthCount; i++) {
            list.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
            calendar.add(Calendar.MONTH, 1);
        }
        map.put("months", list);

        // 查询不同时期的会员数量
        for (String m : list) {
            m = m + ".31";
            Integer count = memberDao.findMemberCountBeforeDate(m);
            memberCount.add(count);
        }
        map.put("memberCount", memberCount);

        return map;
    }

    // 展示前12月的会员数量
    @Override
    public List<Integer> findMemberCountByMonth(List<String> month) {
        List<Integer> list = new ArrayList<>();
        for (String m : month) {
            m = m + ".31";
            Integer count = memberDao.findMemberCountBeforeDate(m);
            list.add(count);
        }
        return list;
    }

    // 分页展示
    @Override
    public Result findPage(Integer currentPage, Integer pageSize, String queryString) throws Exception {
        PageHelper.startPage(currentPage,pageSize);
        Page<Member> members = memberDao.selectByCondition(queryString);
        List<Member> result = members.getResult();
        for (Member member : result) {

            String birthday_str = DateUtils.parseDate2String(member.getBirthday());
            String regTime_str = DateUtils.parseDate2String(member.getRegTime());
            member.setBirthday_str(birthday_str);
            member.setRegTime_str(regTime_str);
        }
        return new Result(true, MessageConstant.QUERY_MEMBER_SUCCESS,new PageResult(members.getTotal(),result));
    }

    // 编辑会员信息
    @Override
    public void edit(Member member) {
        memberDao.edit(member);
    }

    // 据用户id删除一条会员信息
    @Override
    public void deleteById(Integer id) {
        memberDao.deleteById(id);
    }

    //编辑的数据回显，根据用户id查询一条会员信息
    @Override
    public Member findById(Integer id) {
        return memberDao.findById(id);
    }


    /**
     * 计算跨年月份差
     *
     * @param str1
     * @param str2
     * @return
     * @throws ParseException
     */
    private static int getMonthSpace(String str1, String str2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar bef = Calendar.getInstance();
        Calendar aft = Calendar.getInstance();
        bef.setTime(sdf.parse(str1));
        aft.setTime(sdf.parse(str2));
        int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
        int month = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;
        int abs = Math.abs(month + result);
        //System.out.println(abs);
        return abs;
    }

}
