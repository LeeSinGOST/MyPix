package com.pixiv.controller.Interceptor;

import com.pixiv.user.Others;
import com.pixiv.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;

/**
 * @author leesin
 */
@Component
public class CheckCookie {
    public static String checkcookie(Cookie cookies[]){
        String username = null;
        String password = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())){
                    username = cookie.getValue();
                }
                if("password".equals(cookie.getName())){
                    password = cookie.getValue();
                }
            }
        }
        if(username!=null && password!=null) {
            if (Users.getUsers().containsKey(username) && Users.getUsers().get(username).getPassword().equals(password)) {
                return username;
            }
        }
        return null;
    }





}
