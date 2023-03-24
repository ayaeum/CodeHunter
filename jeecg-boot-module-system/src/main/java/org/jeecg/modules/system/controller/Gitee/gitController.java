package org.jeecg.modules.system.controller.Gitee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.entity.AppUserInfo;
import org.jeecg.modules.system.mapper.AppUserInfoMapper;
import org.jeecg.modules.system.model.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * 功能描述
 *
 * @author: 周泽龙
 * @date: 2022年07月31日 21:28
 */
@RestController
@RequestMapping("/Git")
@Slf4j
public class gitController {

    @Autowired
    private AppUserInfoMapper appUserInfoMapper;

    /**
     * hello world
     *
     * @param
     * @return
     */
    public static String access_token;

    @GetMapping("/success")
    public String success(@RequestParam("code")String code) {
        //授权码模式

        List<NameValuePair> params2 = new ArrayList<>();
        params2.add(new BasicNameValuePair("grant_type","authorization_code"));
        params2.add(new BasicNameValuePair("code",code));
        params2.add(new BasicNameValuePair("client_id","7d49c90fef9b37e0a42923300e4cfc0786aaaefd80ca8947556cf2d504ecac19"));
        params2.add(new BasicNameValuePair("redirect_uri","http://192.168.5.80:8080/jeecg-boot/Git/success"));
        params2.add(new BasicNameValuePair("client_secret","bee6d3fa023b19da45d46582c798f414288c2aff1e3c6275cd5486d398d7f982"));

        //创建Post请求
        JSONObject jsonObject = HttpClientUtil.createPostHttp("https","gitee.com","/oauth/token",params2);
        access_token = jsonObject.getString("access_token");

        return "授权成功，请关闭页面";
    }

    /**
     * 携带access_token获取用户Watch的仓库信息
     * @param access_token
     * @return
     */
    @GetMapping(value = "/getWatchRepos")
    public static JSONObject getWatchRepos(@RequestParam("access_token")String access_token) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("access_token",access_token));
        params.add(new BasicNameValuePair("sort","created"));
        params.add(new BasicNameValuePair("direction","desc"));
        params.add(new BasicNameValuePair("page","1"));
        params.add(new BasicNameValuePair("per_page","30"));

        String s = HttpClientUtil.createGetHttp("https","gitee.com","/api/v5/user/subscriptions",params);
        JSONArray array =  JSONArray.parseArray(s);

        return JSONObject.parseObject(array.get(0).toString());
    }


    /**
     * 携带access_token获取用户所有的仓库信息
     * @param access_token
     * @return
     */
    @GetMapping(value = "/getAllRepos")
    public static JSONArray getAllRepos(@RequestParam("access_token")String access_token) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("access_token",access_token));
        params.add(new BasicNameValuePair("visibility","all"));
        params.add(new BasicNameValuePair("page","1"));
        params.add(new BasicNameValuePair("per_page","30"));

        String s = HttpClientUtil.createGetHttp("https","gitee.com","/api/v5/user/repos",params);
        JSONArray array =  JSONArray.parseArray(s);

        return array;
    }

//        AccessToken access = new AccessToken();
//        access.setUser_name("周泽龙");
//        access.setAccess_token("accesstoken");
//        Calendar calendar= Calendar.getInstance();
//        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
//        System.out.println(dateFormat.format(calendar.getTime()));

}
