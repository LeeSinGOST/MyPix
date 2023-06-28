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
    @Value("${custom.others.globalcookie}")
    public void setGlobalcookier(String globalcookie) {
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
}
