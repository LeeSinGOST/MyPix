package com.pixiv.controller;

import com.pixiv.notic.QQBot;
import com.pixiv.user.Others;
import com.pixiv.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class login {
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public ModelAndView login() throws IOException {
        ModelAndView mv = new ModelAndView("login");

        return mv;
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Object login(@RequestParam String username , String password, Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws IOException {
        int maxday = 60*60*24* Others.getMaxday();
        String realip = "null";
        String ipAddress = request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)){
            realip =  request.getHeader("Proxy-Client-IP");
        }else if(ipAddress.split(",").length == 1) {
            realip = ipAddress;
        }else {
            String[] num = ipAddress.split(",");
            realip = num[num.length-2];
        }
        if(Users.getUsers().containsKey(username) && Users.getUsers().get(username).getPassword().equals(password)){
            Cookie cookie1 = new Cookie("username",username);
            Cookie cookie2 = new Cookie("password",password);
            if(Others.getQqnum()!=null && Others.getPushurl()!=null) {
                QQBot.sentqq(username + " 登录成功 \nip：" + realip);
            }
            cookie1.setMaxAge(maxday);
            cookie2.setMaxAge(maxday);
            cookie1.setPath(request.getContextPath());
            cookie2.setPath(request.getContextPath());
            response.addCookie(cookie1);
            response.addCookie(cookie2);
            request.setAttribute("userInfo",username);
            response.sendRedirect("/follow");
            return null;
        }else if(Users.getUsers().containsKey(username) && Users.getUsers().get(username).getPassword().equals(password)){
            request.getSession().setAttribute("userInfo", username);
            request.getSession().setMaxInactiveInterval(maxday);
            response.sendRedirect("/follow");
            return null;
        } else{
            String msg = "用户名或密码错误";
            QQBot.sentqq(username +":"+password +" 登录失败 \nip："+realip);
            map.put("msg",msg);
            return "login";
        }
    }

    @RequestMapping(value = "/logout")
    public Object logout(HttpServletRequest request,HttpServletResponse response){

        request.getSession().removeAttribute("userInfo");
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    cookie.setPath(request.getContextPath());
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
        return "redirect:/";
    }
}
