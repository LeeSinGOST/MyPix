package com.pixiv.controller.Interceptor;

import com.pixiv.controller.Utils.Utils;
import com.pixiv.user.Others;
import com.pixiv.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.pixiv.controller.Interceptor.CheckCookie.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;


/**
 * @author leesin
 */
@Component
public class LoginIntercepter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object userInfo = session.getAttribute("userInfo");
        if("GET".equals(request.getMethod())) {
//            Object col = session.getAttribute("col");
//            System.out.println("col = " + col);
//            if (col == null) {
//                System.out.println("set col");
//                session.setAttribute("col", Others.getDefcol());
//            } else {
////                String colc = CheckCookie.checkcookie1(request.getCookies());
////                if (colc == null) {
////                    Cookie cookie3 = new Cookie("col", String.valueOf(Others.getDefcol()));
////                    cookie3.setMaxAge(60 * 60 * 24 * Others.getMaxday());
////                    cookie3.setPath(request.getContextPath());
////                    response.addCookie(cookie3);
////                    session.setAttribute("col", String.valueOf(Others.getDefcol()));
////                } else {
//                    session.setAttribute("col", col);
////                }
//            }
        }
            if (userInfo == null) {
                String username = CheckCookie.checkcookie(request.getCookies());
                if (username != null) {
                    String cookie = Users.getUsers().get(username).getCookie();
                    session.setAttribute("userInfo", username);
                    request.setAttribute("cookie22", cookie);
                    return true;
                }
                response.sendRedirect("/login");
                return false;
            } else {
                String cookie = null;
                if (Users.getUsers().containsKey(userInfo)) {
                    cookie = Users.getUsers().get(userInfo).getCookie();
                    request.setAttribute("cookie22", cookie);
                } else {
                    System.out.println("怎么会事？");
                }

                return true;
            }
        }
    }

