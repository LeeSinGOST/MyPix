package com.pixiv.user;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
/**
 * @author leesin
 */
//    private static int onproxy;
//    private static int pagesize;
//    private static String cookie;
//
//    @Value("${custom.onproxy}")
//    private int onproxy1;
//    @Value("${custom.pagesize}")
//    private int pagesize1;
//    @Value("${custom.globalcookie}")
//    private String globalcookie1;
//
//    @PostConstruct
//    public void getonproxy1() {
//        onproxy = this.onproxy1;
//        pagesize = this.pagesize1;
//        cookie = this.globalcookie1;
//  }
//
//    public static int getOnproxy(){
//        // lockie.zou
//        return onproxy;
//    }
//
//    public static int getPagesize() {
//        return pagesize;
//    }
//
//    public static String getCookie() {
//        return cookie;
//    }
@Configuration
@ConfigurationProperties(prefix = "custom")

public class Users {
    private static Map<String, User> user;
//    public static String getGlobalcookie() {
//        return other.get("globalcookie");
//    }
//
//    public static int getOnproxy() {
//        return Integer.parseInt(other.get("onproxy"));
//    }
//
//    public static int getPagesize() {
//        return Integer.parseInt(other.get("pagesize"));
//    }
    public static Map<String, User> getUsers() {
        return user;
    }
    public void setUsers(Map<String, User> users) {
        user = users;
    }

    public static class User{
        private String password;
        private String cookie;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getCookie() {
            return cookie;
        }

        public void setCookie(String cookie) {
            this.cookie = cookie;
        }
    }
}
