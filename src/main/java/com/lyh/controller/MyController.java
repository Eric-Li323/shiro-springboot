package com.lyh.controller;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyController {

    @RequestMapping({"/","/index"})
    public String toIndex(Model model){
        model.addAttribute("msg","hello,shiro");
        return "index";
    }

    @RequestMapping("/user/add")
    public String add(){
        return "user/add";
    }

    @RequestMapping("/user/update")
    public String update(){
        return "user/update";
    }

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/login")
    public String login(String username,String password,Model model){
        //获取当前的用户
        Subject subject = SecurityUtils.getSubject();
        //封装用户的登录数据
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
//        token.setRememberMe(true); //设置记住我
        try {
            subject.login(token); //执行登录方法（固定写法），如果没有异常就说明成功了
            Subject currentSubject = SecurityUtils.getSubject();
            Session session = currentSubject.getSession();
            session.setAttribute("loginUser",token);
            System.out.println("token =>"+token);
            return "index";
        } catch (UnknownAccountException e){  //用户名不存在
            model.addAttribute("msg","用户名错误");
            return "login";
        } catch (IncorrectCredentialsException e){  //密码错误
            model.addAttribute("msg","密码错误");
            return "login";
        } catch (AuthenticationException e) {
            model.addAttribute("msg","未知错误");
            return "login";
        }
    }

    @RequestMapping("/unauth")
    @ResponseBody
    public String unauthorized(){   //未授权页面自动跳转的页面
        return "未经授权无法访问此页面";
    }

    @RequestMapping("/logout")
    @ResponseBody
    public String logout(){
//        Subject currentSubject = SecurityUtils.getSubject();
//        currentSubject.logout();
        return "退出登录";
    }
}
