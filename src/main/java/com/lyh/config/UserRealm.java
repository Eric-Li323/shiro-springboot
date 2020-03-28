package com.lyh.config;

import com.lyh.pojo.User;
import com.lyh.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

//自定义的UserRealm
public class UserRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了 => 授权doGetAuthorizationInfo");

        //SimpleAuthorizationInfo
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //拿到当前登录的用户
        Subject subject = SecurityUtils.getSubject();
        User currentUser = (User) subject.getPrincipal(); //拿到user对象

        //设置当前用户的权限
        info.addStringPermission(currentUser.getPerms());
        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行了 => 认证doGetAuthorizationInfo");

//        //用户名，密码   数据库中取  （模拟写法）
//        String name= "root";
//        String password = "123456";


        UsernamePasswordToken userToken =(UsernamePasswordToken) token;

        //连接真实数据库
        User user = userService.queryUserByName(userToken.getUsername());
        if(user == null){
            return null;  //UnknownAccountException
        }

//        //账号认证   （模拟写法）
//        if(!userToken.getUsername().equals(name)){
//            return null; //抛出异常  UnknownAccountException  （自动抛出对应的用户不存在的异常）
//        }

        //可以加密：   md5: ac59075b964b0715    MD5盐值加密: ac59075b964b0715+(username的md5)、(或与一个自定义的固定字符串相加后再MD5)
        //密码认证  shiro自动完成
        return new SimpleAuthenticationInfo(user,user.getPwd(),"");
    }
}
