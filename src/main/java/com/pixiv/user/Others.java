package com.pixiv.user;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "custom.others")
public class Others {

    private static String globalcookie;
    private static int onproxy;
    private static int pagesize;
    private static int maxday;
    private static int defcol;
    private static String qqnum;
    private static String pushurl;


    @Value("${custom.others.globalcookie}")
    public void setGlobalcookie(String globalcookie) {
        Others.globalcookie = globalcookie;
    }
    @Value("${custom.others.onproxy}")
    public void setOnproxy(int onproxy) {
        Others.onproxy = onproxy;
    }
    @Value("${custom.others.pagesize}")
    public void setPagesize(int pagesize) {
        Others.pagesize = pagesize;
    }
    @Value("${custom.others.maxday}")
    public void setMaxday(int maxday) {
        Others.maxday = maxday;
    }
    @Value("${custom.others.defcol}")
    public void setDefcol(int defcol) {
        Others.defcol = defcol;
    }
    @Value("${custom.others.qqnum}")
    public void setQqnum(String qqnum) {
        Others.qqnum = qqnum;
    }
    @Value("${custom.others.pushurl}")
    public void setPushurl(String pushurl) {
        Others.pushurl = pushurl;
    }

    public static String getGlobalcookie() {
        return globalcookie;
    }

    public static int getOnproxy() {
        return onproxy;
    }

    public static int getPagesize() {
        return pagesize;
    }

    public static int getMaxday() {
        return maxday;
    }

    public static int getDefcol() {
        return defcol;
    }
    public static String getQqnum() { return qqnum;}
    public static String getPushurl() {return pushurl;}
}
