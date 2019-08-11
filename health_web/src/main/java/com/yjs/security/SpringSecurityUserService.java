package com.yjs.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yjs.pojo.Permission;
import com.yjs.pojo.Role;
import com.yjs.pojo.User;
import com.yjs.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//创建认证管理对象类

@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    //创建认证管理对象方法
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1、调用业务层，根据用户名查询用户对象
        User user = null;
        try {
            user = userService.findUserByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //判断是否查到用户，如果没有，直接返回null
        if (user == null) {
            return null;
        }
        //2、进行授权
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();

        //获得当前对象的角色集合
        Set<Role> roles = user.getRoles();
        //遍历角色集合
        for (Role role : roles) {
            //获得每个角色所属的权限对象
            Set<Permission> permissions = role.getPermissions();
            //遍历权限对象集合
            for (Permission permission : permissions) {
                //把权限存入grantedAuthorityList集合
                grantedAuthorityList.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }

        //创建UserDetails对象返回

        org.springframework.security.core.userdetails.User userDetail =
                new org.springframework.security.core.userdetails.User
                (username,user.getPassword(), grantedAuthorityList);
        return userDetail;
        
    }
}
