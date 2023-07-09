package com.pixiv.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.pixiv.controller.Utils.Utils;
import com.pixiv.user.Others;
import com.pixiv.user.Users;

import net.sf.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import net.sf.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import sun.rmi.runtime.Log;
import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class userController {
    @RequestMapping("/")
    public Object index(){
        return "redirect:/rank/monthly";
    }
    @RequestMapping("/recommend")
    public Object recommend(@RequestAttribute String cookie22,Map<String, Object> map){
        String url0 = "https://www.pixiv.net/ajax/user/extra?lang=zh";
        int num = 0 ;
        int total = Integer.parseInt(JSONObject.fromObject(gethtml(url0,cookie22)).getJSONObject("body").getString("following"));
        if(total>2){
            num=(int)(Math.random()*total+1);
        }
        String tmp = cookie22.substring(cookie22.indexOf("PHPSESSID=")+10);
        String myid = tmp.substring(0,tmp.indexOf("_"));
        String url1 = "https://www.pixiv.net/ajax/user/"+myid+"/following?offset="+num+"&limit=1&rest=show&tag=&acceptingRequests=0&lang=zh";
        String uid = JSONObject.fromObject(gethtml(url1,cookie22)).getJSONObject("body").getJSONArray("users")
                            .getJSONObject(0).getString("userId");
        System.out.println(uid);
        String url2 = "https://www.pixiv.net/ajax/user/"+uid+"/recommends?userNum=20&workNum=3&isR18=true&lang=zh";
        JSONObject json = JSONObject.fromObject(gethtml(url2,cookie22)).getJSONObject("body");
        String[] result = Utils.getInfo(cookie22);

        map.put("name",result[0]);
        map.put("imgurl",result[1]);
        map.put("title","画师推荐");
        map.put("array",getFlJson1(json));
        map.put("flag","recommend");
        map.put("login","true");
//        map.put()
        return "index";
    }
    @RequestMapping(value = "/follow",method = RequestMethod.GET)
    public Object getfol(@RequestAttribute String                cookie22, Map<String, Object> map){
        ModelAndView mv = new ModelAndView("index");
        String tmp = cookie22.substring(cookie22.indexOf("PHPSESSID=")+10);
        String myid = tmp.substring(0,tmp.indexOf("_"));
        String url = "https://www.pixiv.net/ajax/user/"+myid+"/following?offset=0&limit=24&rest=show&tag=&acceptingRequests=0&lang=zh";
        JSONObject json1 = JSONObject.fromObject(gethtml(url,cookie22)).getJSONObject("body");
        int total = json1.getInt("total");
        JSONArray array1 = json1.getJSONArray("users");
        String[] result = Utils.getInfo(cookie22);
        map.put("name",result[0]);
        map.put("imgurl",result[1]);
        map.put("title","我的关注");
        map.put("total",total);
        map.put("array",getFlJson(array1));
        map.put("flag","follow");
        map.put("login","true");
        return mv;
    }
    @RequestMapping(value = "/bookmark", method = RequestMethod.GET)
    public Object bookmark(@RequestAttribute String cookie22,Map<String, Object> map){
        String tmp = cookie22.substring(cookie22.indexOf("PHPSESSID=")+10);
        String myid = tmp.substring(0,tmp.indexOf("_"));
        String URL  = "https://www.pixiv.net/ajax/user/"+myid+"/illusts/bookmarks?tag=&offset=0&limit=40&rest=show&lang=zh";
        String html1 = gethtml(URL,cookie22);
        JSONArray json1=JSONObject.fromObject(html1).getJSONObject("body").getJSONArray("works");
        String[] result = Utils.getInfo(cookie22);
        map.put("name",result[0]);
        map.put("imgurl",result[1]);
        map.put("title","我的收藏");


        map.put("array",getjson3(json1));
        map.put("login","true");
        return "rank";
    }
    @RequestMapping(value = "/bookmark/{id}", method = RequestMethod.GET)
    public Object bookmarks(@PathVariable String id,String page, Map<String, Object> map){
        int size = 40;
        int offset = 0;
        if(page != null){
            try {
                if(Integer.parseInt(page)>0){
                    offset = Integer.parseInt(page) * size;
                }
            } catch(Exception e){
                e.printStackTrace();
            }

        }
        String URL  = "https://www.pixiv.net/ajax/user/"+id+"/illusts/bookmarks?tag=&offset="+offset+"&limit=40&rest=show&lang=zh";
        String html1 = gethtml(URL,Others.getGlobalcookie());
        JSONArray json1=JSONObject.fromObject(html1).getJSONObject("body").getJSONArray("works");
        JSONObject json2=JSONObject.fromObject(html1).getJSONObject("body").getJSONObject("extraData").getJSONObject("meta").getJSONObject("ogp");
        String title = json2.getString("title");
        String image = json2.getString("image");
        map.put("name",title.substring(0, title.length() - 3));
        map.put("imgurl",image.substring(20));
        map.put("title",title);

        map.put("array",getjson3(json1));
        return "rank";
    }
    @RequestMapping(value = "/bookmark", method = RequestMethod.PUT)
    @ResponseBody
    public Object addbookmark(@RequestAttribute(required = false) String cookie22,@RequestParam String page){
        int size = 40;
        int offset = (Integer.parseInt(page)-1)*size;
        String tmp = cookie22.substring(cookie22.indexOf("PHPSESSID=")+10);
        String myid = tmp.substring(0,tmp.indexOf("_"));
        String URL  = "https://www.pixiv.net/ajax/user/"+myid+"/illusts/bookmarks?tag=&offset="+offset+"&limit="+size+"&rest=show&lang=zh";
        String html1 = gethtml(URL,cookie22);
        JSONArray json1=JSONObject.fromObject(html1).getJSONObject("body").getJSONArray("works");

        return getjson3(json1);
    }
    @RequestMapping(value = "/bookmark/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Object addbookmarks(@RequestParam String page,@PathVariable String id){
        int size = 40;
        int offset = (Integer.parseInt(page)-1)*size;
        String URL  = "https://www.pixiv.net/ajax/user/"+id+"/illusts/bookmarks?tag=&offset="+offset+"&limit="+size+"&rest=show&lang=zh";
        String html1 = gethtml(URL,Others.getGlobalcookie());
        JSONArray json1=JSONObject.fromObject(html1).getJSONObject("body").getJSONArray("works");

        return getjson3(json1);
    }

    @RequestMapping(value = "/update",method = RequestMethod.GET)
    public Object index(@RequestAttribute String cookie22,Map<String, Object> map){
        String url = "https://www.pixiv.net/ajax/follow_latest/illust?p=1&mode=all&lang=zh";
        JSONArray picinfo = JSONObject.fromObject(gethtml(url,cookie22))
                .getJSONObject("body")
                .getJSONObject("thumbnails")
                .getJSONArray("illust");
        JSONArray jsonArray = getJson2(picinfo,1);
        String[] result = Utils.getInfo(cookie22);
        map.put("name",result[0]);
        map.put("imgurl",result[1]);
        map.put("array",jsonArray);

        map.put("login","true");
        map.put("mode1","最近更新");
        return "rank";
    }
    @RequestMapping(value = "/pic/{pid}")
    public Object getpic(@PathVariable String pid,Map<String, Object> map){
        JSONObject json = getPicinfo(pid);
        map.put("picinfo",json);
        return "pic";
    }
    @RequestMapping(value = "/test/{pid}")
    public Object getpic1(@PathVariable String pid,Map<String, Object> map){
        JSONObject json = getPicinfo(pid);
        map.put("picinfo",json);
        map.put("pid",pid);
        return "test1";
    }
    @RequestMapping(value = "/id/{id}",method = RequestMethod.GET)
    public Object test(@RequestAttribute(required = false) String cookie22,@PathVariable String id, String page, Map<String, Object> map){

        if(cookie22 == null){
            cookie22 = Others.getGlobalcookie();
        }
        JSONObject uidinfo = JSONObject.fromObject(getUid(id,cookie22));
        List<String> piclist= getPidlist(id,page);
        map.put("array",getjson1(piclist,id));
        map.put("page",page);
        map.put("id",id);
        map.put("login","true");
        map.put("imgurl",uidinfo.getString("imageBig").substring(20));
        map.put("isFollow",uidinfo.getString("isFollowed"));
        map.put("username",uidinfo.getString("name"));
        return "rank1";

    }
    @RequestMapping(value = "/id/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object checkid(@PathVariable String id){

        if(getPidlist(id, "1")==null){
            return 0;
        }else {
            return 1;
        }
    }
    @RequestMapping(value = "/id/{id}",method = RequestMethod.PUT)
    @ResponseBody
    public Object addimg(@PathVariable String id, String page){
        return getjson1(getPidlist(id,page),id);
    }

    @RequestMapping(value = "/pid/{pid}")
    @ResponseBody
    public void getimg(HttpServletResponse response, @PathVariable String pid,
                       @RequestParam(required = false,defaultValue = "0") String num) throws IOException {
        String url1 = "https://www.pixiv.net/ajax/illust/"+pid+"?lang=zh";
        String html2 = gethtml(url1,Others.getGlobalcookie());
        JSONObject json=JSONObject.fromObject(html2).getJSONObject("body");
        String picurl = json.getJSONObject("urls").getString("original");
        if(!num.equals("0")){
            picurl = picurl.replace("_p0","_p"+num);
        }
        InputStream in = getimage(picurl);

        BufferedImage bufferedImage = ImageIO.read(in);
        String format = picurl.substring(picurl.lastIndexOf(".") + 1);;
        ImageIO.write(bufferedImage,format,response.getOutputStream());
	 response.getOutputStream().close();
//        response.setContentType("image/jpg");
//        OutputStream out = response.getOutputStream();
//        byte[] buff = new byte[100];
//        int rc = 0;
//        while ((rc = in.read(buff, 0, 100)) > 0) {
//            out.write(buff, 0, rc);
//        }
//        out.flush();
    }
    @RequestMapping(value = "/img/**")
    @ResponseBody
    public void getimg2(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String picurl = "https://i.pximg.net/"+request.getRequestURI().substring(5);
        InputStream in = getimage(picurl);
        BufferedImage bufferedImage = ImageIO.read(in);
        String format = picurl.substring(picurl.lastIndexOf(".") + 1);;
        ImageIO.write(bufferedImage,format,response.getOutputStream());

    }

    @RequestMapping(value ="/rank/{mode}",method = RequestMethod.GET)
    public Object getrank(@PathVariable String mode, String date,Map<String, Object> map) throws ParseException {
        String URL;
        if (date==null){
            URL = "https://www.pixiv.net/ranking.php?format=json";
        }else {
            URL = "https://www.pixiv.net/ranking.php?format=json&date="+date;
        }
        if(mode.indexOf("ai")==-1 && mode.indexOf("male")==-1){
            URL = URL+"&content=illust&mode="+mode;
        }
        else{
            URL = URL+"&mode="+mode;
        }
        JSONObject test = JSONObject.fromObject(gethtml(URL,Others.getGlobalcookie()));

        JSONArray jsonArray = test.getJSONArray("contents");
        String mode1 = test.getString("mode");
        String date1 = test.getString("date");
        String prevdate = test.getString("prev_date");
        String nextdate = test.getString("next_date");
        String ranktotal = test.getString("rank_total");


        JSONArray jsonArray1 = getJson2(jsonArray,0);
        switch (mode){
            case "monthly":mode1="月榜";break;
            case "weekly":mode1="周榜";break;
            case "daily":mode1="日榜";break;
            case "daily_ai":mode1="AI榜";break;
            case "daily_r18_ai":mode1="AI榜R";break;
            case "daily_r18":mode1="日榜R";break;
            case "weekly_r18":mode1="周榜R";break;
        }
        map.put("array",jsonArray1);
        map.put("mode",mode);
        map.put("login","true");
//        map.put("col",col);
        map.put("mode1",mode1);
        map.put("date",date1);
        map.put("prevdate",prevdate);
        map.put("ranktotal",ranktotal);
        map.put("nextdate",nextdate);

        return "rank";
    }
    @RequestMapping(value ="/rank/{mode}",method = RequestMethod.PUT)
    @ResponseBody
    public Object addRank(@RequestParam String mode1, String page){

        String URL = "https://www.pixiv.net/ranking.php?mode="+mode1+"&content=illust&p="+page+"&format=json";
        return getJson2(JSONObject.fromObject(gethtml(URL,Others.getGlobalcookie())).getJSONArray("contents"),0);

    }
    @RequestMapping(value = "/follow",method = RequestMethod.POST)
    @ResponseBody
    public Object follow(@RequestAttribute String cookie22,@RequestParam String id , String flag) {
        BufferedReader reader = null;
        String result = "";
        String url = "";
        String burl = "https://www.pixiv.net/bookmark_add.php";
        String content = "";
        String Contype = "";
        if(flag.equals("1")) {
            url = "https://www.pixiv.net/bookmark_add.php";
//            content = "mode=add&type=user&user_id=" +id+ "&tag=&restrict=0&format=json";
            content = "mode=add&type=user&user_id=" +id+ "&tag=&restrict=0&format=json&recaptcha_enterprise_score_token=03AD1IbLCUdVzkAihXVdAEKqa1kXvul7rTv5PknqAQuiGxPt3gMrZrg2_idEC0BgBul-2f3NoWh-WTiT6Dmh-yUGQsth-dEExqepiMXetqw8vsX64zHJ0wE9T1hpHcMGyqsGSuu-1V7eDNnfWHfJ-tlfYhL41qCZakxiFftzF-jb65flsPiggrAKlgPby3hY7VRD31QTNNsO7Bk2IFFLEPlB9x6cizAzHLS6MWGGWexOcAwsWKwInHS5OM8sir-haGJN834jc86B8jAMmb40brOuzzskHaBup-Y01ahFV2hm1HlrVsxBlAqtR1eYGVTnYPBpwp2v49-CjnEpXSb3UQDLsXWErUmXlvk8EC2lZHC2tjnG_LWSSIL_r0WqyXlZPdlq9rT14ynT0I10tnS8Kn1rcJnyJZ-t1uUTRAhBLoO-9KA3jczXcoMBWLcl3qAsOj_eQJc00P0OGNOojpRVfTVDMbM4g_xs5B3IUZcaHnfizYHfEanWaKjnW_OcKFcuNLHUV3UzedaLuJjWrUSyORbXm602VZ05ZQEkPXTxq6o8qb4bvxfn7-HzJPYH6oLG3eqP4Qzq8w8EIREHT0c85hhx-2b7NUxsPApi1ofXJBQdfJ62QyDeMW_ZFhF_RFMAV3ra-uFPZcUqxsmD0wPyy9LOzcwqh6j2XS1flHL-sSdBfl6Egf67N8G0cJNrTpzmHRqKScJAsjXI2NQ6-IabPkXyD6_sm97qP-Kk03NCyzetvPGcPwkvcqWZnfd585Y-cUT_C9pEdiy_zNtUu3L22q8vujIaJmpQPp99AAx0MjewcNc9ZHYbPniD2_AhunFJGkcia0seBgqEhKkBb5kSlkXJJyiXdxso-CXFkYnI25KWH3_tWQVxvE6QQtSaoB0ZuRw7LXm9GhuzLyGGg6xy4XOWfPwwmco77-4M3NhL0jXIvJd3E04eyALRk37f5QH04hdYX2zKbHuG_1WqmUq2_5xnmUsM4p0jctaMAgVx3pIZ4syu7WIgpFwllOE3dXV5tobVndYzBoh-9-gIe13j-b7yZqrxYAcuHg7xmV5OrHjHPA1LFPmB0n6A39qc2Jdo-g3dHQESE3rlkRVNg223DLk5fjiunOUYkYjvZjsS7INyFoXm8h5R7tQ5NU8v1IkgoGN6HIaiVaXki6QnNARcvRTe91Ffda89H5ya9EZ5QbV6zkcPSBZlziKwUY0qRWLualp5E_nqEAV8-nr-13TR_IrlKEX3kvG39iF3Yau02NUOlQeMxkaveMUd3LFg3jhxpe7GbUAuVPBVmYaMlIBoI9va_QLpLfsbXzRlLr3G3PziYmIX4hk29wKLyP2XGXNLVYGHU088KiemcCtSsEdiVDRtrYM3jnzF2e2Pj8QvI_6ff4g49EbrDzrq5_ECqs_AqNcrkxlmGu_f1TSVfN3YBkP04FUHLitNalTzYK8lkf0K6qwvDkTw2b50RUAuDcDw7_HTpuaabybV9m59QzmJjvljkOV_6CR7SouB2kxG17TpaH5uWjNmb0HMCNgKfjxxrdFCB4T_YV4Yw0--LG-X0i3cwMLuXvEsWkarybwcDwcn3ajKzDvmHZBG3BFHm5lGOUTr10FJyHHhmEABmZj7EeWX_U1O-4HlXmE1LFEVb8j9rEpov8kdLXjflmRm-Ha7090L1L60f6gEdrDQpgAPs0VBhFFm2e7Q5EGvRX1ks4lOXuYeHN7pbZGQE9Bjw4onGZrmMoEQOO9ojuDvoN_Or92aBraSVHaTAmC2CfbAUNVW3c2rGFtU7i8ksqGzd8kaMmOP0dpkqcpjxKvbjZ7oI8aqtcV9zxUNtVNGRkNg3veOnOKKgRZdOfFQd_aVumMububPN-ZqdPLhtcApOSGUgPmN82zyyt8MHnA01YfInNup-5jWy13dh_zG_Qtnht8j22tDhkiXIVyWYMwYTtC3dFxTo5GQy0PZEmpLKK2odUA_K0BCJHYo8KGpsZALoe8rmSzNyVr4liLhP2Uc5_OY6wTDr6llgS83c3Cl-KfbGE4_kLEG2kF3QJ5LdH3oI5k3-cWqmU5XjURheOKwVCc12_VvWJtlVRfBbyqJaPRnNYg8JQV0ll_dyQjsGn9bU--hFcAc6icZxfcRRKlaz9LcPT6WM29IAkQHdqlCS_UjthzwD2ugagejyJWKgXXSC-cuXOv5VNyDRc5I_RkbtY4QJ6SIC0CPNZrf0JCoTdHOqAlAGpiQlNEAnFYXvTvCLmxo0mWWPdDtT3CO49kUvsz1citLmK8IRlUo8Npu9M0V3UgzWu8NZRPTYZhof9N_f4YX_cuuGfwcXV3sNLsJuAMy5sWuMRfaUVX-XzvFNS1LKpMrS6ia9H4RRN0B1H7tPS1Isz4ErbPz0cfinFcqeDdacB4gbIPxn_Pok2YNMDTjF_CfT47F89opz3rS-FGn1hQB2CNUBnlU54MBdQtGCRGZ7a65nQ9q2gPTsV6JHou-9fI-cyWhYd2K8NYympjL3FVY1TgkIyUGQ2nmuhEDKCbBV9IRqM1ZTsO6J9hdKqA5paxrrv7t20OUbIx7Rbsx39OX4IPW_ur7AWcyQNoblt7hHAnmvgZ2Ee_a2XcpPLfk0ZPszVMOlAC1FObzMwjgSQ0_rKAz3ZX-7zOsZGKsAY9Q75v8-kepeMx8aM1HyguryzdbblJhStglO6pTnLDU2Gsy-_Nkxbe23ZfKlvo101UUof4sP2yRfdiNPlzttOgPLe4aHvZlgKG0X9wCMi6xKqw7hraw__0AdIfv4gjrAAcRhASiO1RZxL8wtbdH56F4cUyDOBbCLMamRCuf8zTlEjDMeuuc3Hh6huzHuq";
            Contype = "application/x-www-form-urlencoded; charset=utf-8";

        }else if(flag.equals("2")){
            url = "https://www.pixiv.net/ajax/illusts/bookmarks/add";
            content = "{\"illust_id\":\""+id+"\",\"restrict\":0,\"comment\":\"\",\"tags\":[]}";
            Contype = "application/json; charset=utf-8";
        }
        try {
            String html = gethtml(burl,cookie22);
            String content1 = Jsoup.parse(html).select("meta[id=meta-global-data]").attr("content");
            String token = JSONObject.fromObject(content1).getString("token");

            HttpsURLConnection conn;

            if (Others.getOnproxy() == 1) {
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 17890));
                URL urls = new URL(url);
                conn = (HttpsURLConnection) urls.openConnection(proxy);

            } else {
                URL urls = new URL(url);
                conn = (HttpsURLConnection) urls.openConnection();

            }
            conn.setRequestProperty("Cookie",cookie22);
            conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36");
            conn.setRequestProperty("origin", "https://www.pixiv.net/");
            conn.setRequestProperty("accept", "application/json");
            conn.setRequestProperty("x-csrf-token", token);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setInstanceFollowRedirects(true);
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            conn.setRequestProperty("Content-Type", Contype);
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(5000);
//        conn.connect();
            byte[] writebytes = content.getBytes();
            // 设置文件长度
            conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
            System.out.println(writebytes.length);
            OutputStream outwritestream = conn.getOutputStream();
            outwritestream.write(content.getBytes());
            outwritestream.flush();
            outwritestream.close();
            if (conn.getResponseCode() == 200) {
                reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                result = reader.readLine();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(result.equals("[]")){

            return 1;
        }
        return result;




//        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
//        out.writeBytes(content);
//        out.flush();
//        out.close();
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        String line;
//
//        while ((line = reader.readLine()) !=null){
//            System.out.println(line);
//        }
//
//        reader.close();
//        conn.disconnect();

    }
    @RequestMapping(value = "/follow",method = RequestMethod.PUT)
    @ResponseBody
    public Object addfollow(@RequestAttribute String cookie22,@RequestParam String page){
        int size = 24;
        int offset = (Integer.parseInt(page)-1)*size;
        String tmp = cookie22.substring(cookie22.indexOf("PHPSESSID=")+10);
        String myid = tmp.substring(0,tmp.indexOf("_"));
        String url = "https://www.pixiv.net/ajax/user/"+myid+"/following?offset="+offset+"&limit="+size+"&rest=show&tag=&acceptingRequests=0&lang=zh";
        JSONArray array1 = JSONObject.fromObject(gethtml(url,cookie22)).getJSONObject("body").getJSONArray("users");
        return getFlJson(array1);

    }
    @RequestMapping(value = "/recommend",method = RequestMethod.PUT)
    @ResponseBody
    public Object addrecommend(@RequestAttribute String cookie22){
        String url0 = "https://www.pixiv.net/ajax/user/extra?lang=zh";
        int num = 0 ;
        int total = Integer.parseInt(JSONObject.fromObject(gethtml(url0,cookie22)).getJSONObject("body").getString("following"));
        if(total>2){
            num=(int)(Math.random()*total+1);
            System.out.println("num = " + num);
        }
        String tmp = cookie22.substring(cookie22.indexOf("PHPSESSID=")+10);
        String myid = tmp.substring(0,tmp.indexOf("_"));
        String url1 = "https://www.pixiv.net/ajax/user/"+myid+"/following?offset="+num+"&limit=1&rest=show&tag=&acceptingRequests=0&lang=zh";
        String uid = JSONObject.fromObject(gethtml(url1,cookie22)).getJSONObject("body").getJSONArray("users")
                .getJSONObject(0).getString("userId");
        System.out.println(uid);
        String url2 = "https://www.pixiv.net/ajax/user/"+uid+"/recommends?userNum=20&workNum=3&isR18=true&lang=zh";
        JSONObject json = JSONObject.fromObject(gethtml(url2,cookie22)).getJSONObject("body");
        return getFlJson1(json);

    }


    public  JSONArray getJson2(JSONArray jsonArray, int flag){
        String title = null;
        String pid = null;
        String uid = null;
        String pname = null;
        String height;
        String width;
        String num;
        String rank;
        int size = jsonArray.size();
        String[] iddd2 = {"illust_id","title","user_id","user_name","width","height","illust_page_count","rank"};
        String[] iddd1  = {"pid","title","uid","username","width","height","num","rank"};
        String[] iddd3 = {"id","title","userId","userName","width","height","pageCount"};
        JSONArray jsonArray1 = new JSONArray();
        JSONObject content = null;
        if(flag == 0) {
            for (int i = 0; i < size; i++) {
                content = new JSONObject();

                for (int j = 0; j < iddd2.length; j++) {
                    content.put(iddd1[j], jsonArray.getJSONObject(i).getString(iddd2[j]));
                }
                jsonArray1.add(content);
            }
        }else if(flag == 1){
            for (int i = 0; i < size; i++) {
                content = new JSONObject();
                for (int j = 0; j < iddd3.length; j++) {
                    content.put(iddd1[j], jsonArray.getJSONObject(i).getString(iddd3[j]));
                }

                jsonArray1.add(content);
            }
        }

        return jsonArray1;
    }
    public  JSONObject getUid(String id,String cookie){
        String uri = "https://www.pixiv.net/users/"+id;
        String html = gethtml(uri,cookie);
        String content = Jsoup.parse(html).select("meta[name=preload-data]").attr("content");
        return JSONObject.fromObject(content).getJSONObject("user").getJSONObject(id);
    }
    public  List<String> getPidlist(String id, String page){
        String uri = "https://www.pixiv.net/ajax/user/"+id+"/profile/all?lang=zh";
        String html = gethtml(uri,Others.getGlobalcookie());
        if(html == null){
            return null;
        }
        JSONObject piclist = JSONObject.fromObject(html);
        try{
            piclist = piclist.getJSONObject("body").getJSONObject("illusts");
        }catch (Exception e){
            return null;
        }
        Iterator it = piclist.keys();
        List<String> keyListstr = new ArrayList<String>();
        while(it.hasNext()) {
            keyListstr.add(it.next().toString());
        }
        int page1=1;
        int size = keyListstr.size();
        int max,min;
        if(page != null){
            page1 = Integer.parseInt(page);
        }
        int pagesize = Others.getPagesize();
        min = (page1-1)*pagesize;
        if(min > size){
            return null;
        }
        max = size-min>=pagesize?page1*pagesize:size;
        keyListstr = keyListstr.subList(min,max);
        return keyListstr;
    }
    public  JSONArray getjson1(List<String> keyListstr,String id){
        String title = null;
        String pid = null;
        String height;
        String width;
        String num;


        JSONArray jsonArray1 = new JSONArray();
        String ids = "";
        for(String str: keyListstr){
            ids = ids + "ids[]=" + str + "&";
        }
        String newurl =  "https://www.pixiv.net/ajax/user/"+id+"/profile/illusts?"+ids+"work_category=illustManga&is_first_page=0&lang=zh";
        String html1 = gethtml(newurl,Others.getGlobalcookie());
        JSONObject json1=JSONObject.fromObject(html1).getJSONObject("body").getJSONObject("works");
        JSONObject content;
        for(String id1:keyListstr){
            content = new JSONObject();
            pid = id1;
            width = json1.getJSONObject(id1).getString("width");
            height = json1.getJSONObject(id1).getString("height");
            num = json1.getJSONObject(id1).getString("pageCount");
            title = json1.getJSONObject(id1).getString("title");
            content.put("pid",pid);
            content.put("title",title);
            content.put("width",width);
            content.put("height",height);
            content.put("num",num);
            jsonArray1.add(content);
        }



        return jsonArray1;

    }
    public  JSONArray getjson3(JSONArray json1){
        String title = null;
        String pid = null;
        String height;
        String width;
        String uid;
        String num;
        String username;
        String url;
        JSONArray jsonArray1 = new JSONArray();
        JSONObject content;
        int size = json1.size();
        for(int i=0;i<size;i++){
            content = new JSONObject();
            pid = json1.getJSONObject(i).getString("id");
            uid = json1.getJSONObject(i).getString("userId");
            username = json1.getJSONObject(i).getString("userName");
            width = json1.getJSONObject(i).getString("width");
            height = json1.getJSONObject(i).getString("height");
            num = json1.getJSONObject(i).getString("pageCount");
            title = json1.getJSONObject(i).getString("title");
//            url = json1.getJSONObject(i).getString("url").substring(20);
            url = json1.getJSONObject(i).getString("url").substring(36);
//            if(json1.getJSONObject(i).has("urls")){
//                url = json1.getJSONObject(i).getJSONObject("urls").getString("540x540").substring(20);
//            }
            content.put("pid",pid);
            content.put("uid",uid);
            content.put("username",username);
            content.put("title",title);
            content.put("url",url);
            content.put("width",width);
            content.put("height",height);
            content.put("num",num);
            jsonArray1.add(content);
        }
        return jsonArray1;
    }
    public  JSONArray getFlJson(JSONArray json1){
        String title = null;
        String uid;
        String username;
        String profileimg;
        String follow;
        String comment;
        int size = json1.size();
        JSONObject content;
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArray2 = new JSONArray();
        for(int i=0;i<size;i++){
            content = new JSONObject();

            uid = json1.getJSONObject(i).getString("userId");
            username = json1.getJSONObject(i).getString("userName");
            follow = json1.getJSONObject(i).getString("following");
            profileimg = json1.getJSONObject(i).getString("profileImageUrl").substring(20);
            comment = json1.getJSONObject(i).getString("userComment");
            jsonArray2 = getjson3(json1.getJSONObject(i).getJSONArray("illusts"));

            content.put("uid",uid);
            content.put("username",username);
            content.put("title",title);
            content.put("profileimg",profileimg);
            content.put("works",jsonArray2);
            content.put("follow",follow);
            content.put("comment",comment);

            jsonArray.add(content);
        }
        return jsonArray;
    }
    public  JSONArray getFlJson1(JSONObject json) {
        String title = null;
        String uid;
        String username;
        String profileimg;
        String follow;
        String comment;
        JSONObject content;
        JSONArray jsonArray = new JSONArray();
        JSONArray json1 = json.getJSONArray("users");
        JSONArray json2 = json.getJSONObject("thumbnails").getJSONArray("illust");
        int size = json1.size();
        JSONArray jsonArray2 = new JSONArray();
        for (int i = 0; i < size; i++) {
            content = new JSONObject();
            JSONArray jsonArray1 = new JSONArray();
            uid = json1.getJSONObject(i).getString("userId");
            username = json1.getJSONObject(i).getString("name");
            follow = "false";
            profileimg = json1.getJSONObject(i).getString("imageBig").substring(20);
            comment = json1.getJSONObject(i).getString("comment");
            for (int j = 0; j < 3; j++) {
                jsonArray1.add(json2.getJSONObject(3 * i + j));
            }
            jsonArray2 = getjson3(jsonArray1);
            content.put("uid",uid);
            content.put("username",username);
            content.put("title",title);
            content.put("profileimg",profileimg);
            content.put("works",jsonArray2);
            content.put("follow",follow);
            content.put("comment",comment);
            jsonArray.add(content);
        }
        return jsonArray;
    }
    public static String gethtml(String url,String cookie){

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies = new ArrayList<>();

        List<String> ua = new ArrayList<>();
        List<String> ref = new ArrayList<>();
        cookies.add(cookie);
        ua.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Safari/537.36");
        ref.add("https://www.pixiv.net/");
        headers.put(HttpHeaders.COOKIE,cookies);
        headers.put(HttpHeaders.USER_AGENT,ua);
        headers.put(HttpHeaders.REFERER,ref);
        headers.setContentType(MediaType.parseMediaType("application/json;charset=UTF-8"));
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        if(Others.getOnproxy() == 1) {
            System.setProperty("java.net.useSystemProxies", "true");
            simpleClientHttpRequestFactory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 17890))); // 添加代理 ip 和 port 即可
        }
        restTemplate.setRequestFactory(simpleClientHttpRequestFactory);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        }catch (Throwable e){
            return null;
        }
        return response.getBody();
    }
    public  InputStream getimage(String picurl) throws IOException {
        URLConnection conn;
        if(Others.getOnproxy() == 1) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 17890));

            URL urls = new URL(picurl);
            conn = urls.openConnection(proxy);
        }else{
            URL urls = new URL(picurl);
            conn = urls.openConnection();
        }


        conn.setConnectTimeout(1000);
        conn.setReadTimeout(5000);
        conn.setRequestProperty("Cookie",Others.getGlobalcookie());
        conn.setRequestProperty("referer","https://www.pixiv.net/");
        conn.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36");
        conn.connect();
        return conn.getInputStream();
    }
    public JSONObject getPicinfo(String pid){
        String url1 = "https://www.pixiv.net/ajax/illust/"+pid+"?lang=zh";
        String html2 = gethtml(url1,Others.getGlobalcookie());
        JSONObject json=JSONObject.fromObject(html2).getJSONObject("body");
        String picurl = json.getJSONObject("urls").getString("original");
        String uid = json.getString("userId");
        JSONObject uidinfo = JSONObject.fromObject(getUid(uid,Others.getGlobalcookie()));
        JSONObject json4 = new JSONObject();
        json4.put("title",json.getString("title"));
        json4.put("uid",uid);
        json4.put("descri",json.getString("description"));
        json4.put("username",json.getString("userName"));
        json4.put("imgurl",uidinfo.getString("imageBig").substring(20));
        json4.put("picurl",picurl.substring(20,picurl.length()-5));
        json4.put("extend",picurl.substring(picurl.length()-4));
        json4.put("width",json.getString("width"));
        json4.put("height",json.getString("height"));
        json4.put("num",json.getInt("pageCount"));
        json4.put("like",json.getInt("likeCount"));
        json4.put("bookmark",json.getString("likeData"));
        return json4;
    }

}
