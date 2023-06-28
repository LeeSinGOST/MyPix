package com.pixiv.notic;



import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import java.io.IOException;

public class QQBot {

    public static void sentqq(String content) throws IOException {
        String url = "https://push.kokutou.top/sent";
        String qid = "729076660";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",qid);
        jsonObject.put("content",content);
        jsonObject.put("from","pixiv");
        //创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();
        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);
        //装填参数

        StringEntity s = new StringEntity(jsonObject.toString(), "utf-8");
//        s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
//                "application/json"));

        //设置参数到请求对象中
        httpPost.setEntity(s);
        httpPost.setHeader("Content-Type", "application/json");
        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(httpPost);
        //获取结果实体
        HttpEntity entity = response.getEntity();
        EntityUtils.consume(entity);
        //释放链接
        response.close();



    }

}
