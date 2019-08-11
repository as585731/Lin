package com.yjs.jobs;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yjs.constant.RedisConstant;
import com.yjs.service.OrderSettingService;
import com.yjs.utlis.DateUtils;
import com.yjs.utlis.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

//删除七牛云的多余图片
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    OrderSettingService orderSettingService;
    
    
    //清理图片
    public void cleanImg(){
        //获得redis内两个集合的差值
        Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        //循环，拿出所有差值
        for (String img : sdiff) {
            System.out.println(img);
            //删除七牛云上的对应文件
            QiniuUtils.deleteFileFromQiniu(img);
            //删除redis内的对应数据
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,img);
            System.out.println("图片清理成功");
        }
    }
    
   
    


}
