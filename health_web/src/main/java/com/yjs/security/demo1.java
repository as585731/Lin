package com.yjs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class demo1 {
    

    public static void main(String[] args) throws ParseException {
       //new demo1().fun1();
        List<String> month = new ArrayList<>();
        //根据指定时间创建一个日历对象
        Calendar calendar = Calendar.getInstance();
        //将指定时间转为date对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date parse = sdf.parse("2018-07");
        Date parse2 = sdf.parse("2019-08");
        
        //将时间设置为日历对象
        calendar.setTime(parse);
        //判断日历的对象时间是否小于等于结束的时间
        while (calendar.getTime().compareTo(parse2)<1) {
            //如果小于等于则月份存入集合
            month.add(sdf.format(calendar.getTime()));
            System.out.println(sdf.format(calendar.getTime()));
            //月份+1
            calendar.add(Calendar.MONTH,1);
        }
        
        
        
        
    }
    
    void fun1() throws ParseException {
        //BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        //System.out.println(bCryptPasswordEncoder.encode("123"));
        


    }

}
