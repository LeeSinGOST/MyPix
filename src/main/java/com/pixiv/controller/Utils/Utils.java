package com.pixiv.controller.Utils;

import com.pixiv.user.Users;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.pixiv.controller.userController.gethtml;

public class Utils {
    public static String[] getInfo(String cookie){
        String url = "https://www.pixiv.net/";
        String html = gethtml(url,cookie);
        JSONObject js = JSONObject.fromObject( Jsoup.parse(html).select("meta[id=meta-global-data]").attr("content")).getJSONObject("userData");
        String name = js.getString("name");
        String imgurl = js.getString("profileImgBig").substring(20);
        String[] result = {name,imgurl};
        return result;
    }

}


