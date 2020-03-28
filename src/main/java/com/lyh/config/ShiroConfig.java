package com.lyh.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    //shiroFilterFactoryBean     （第三步）
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager666") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置安全管理器
        bean.setSecurityManager(defaultWebSecurityManager);

        //添加shiro的内置过滤器
        /**
         * anon:  无需认证即可访问
         * authc:  必须认证才能访问
         * user:   必须拥有 记住我 功能才能使用
         * perms:  必须拥有 对某个资源的权限才能访问
         * role :  拥有某个角色权限才能访问
         */

        //拦截
        Map<String, String> filterMap = new LinkedHashMap<>();

        //授权，正常情况下，没有授权将跳转到为授权页面        perms[角色：权限]
        filterMap.put("/user/add","perms[user:add]");
        filterMap.put("/user/update","perms[user:update]");

        //拦截
        filterMap.put("/user/*","authc");

        //退出登录  (可以没有页面)
        filterMap.put("/logout","logout");

        bean.setFilterChainDefinitionMap(filterMap);

        //设置登录请求（在访问失败的情况下自动跳转到登录界面）
        bean.setLoginUrl("/toLogin");
        //设置未授权跳转页面
        bean.setUnauthorizedUrl("/unauth");


        return bean;
    }

    //DefaultWebSecurityManager    （第二步）
    @Bean(name = "securityManager666")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联UserRealm
        securityManager.setRealm(userRealm);

        return securityManager;
    }

    //创建realm 对象，需要自定义类:  （第一步）
    @Bean
    public UserRealm userRealm(){
        return new UserRealm();
    }

    //整合shiroDialect: 用来整合shiro 与 thymeleaf
    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }
}
