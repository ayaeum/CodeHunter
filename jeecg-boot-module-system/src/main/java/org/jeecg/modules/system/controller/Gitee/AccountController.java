package org.jeecg.modules.system.controller.Gitee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jeecg.modules.system.model.HttpClientUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述
 *
 * @author: 周泽龙
 * @date: 2022年08月06日 20:51
 */
@RestController
@RequestMapping("/Git")
@Slf4j
public class AccountController {

    @GetMapping(value = "/getUserInfo")
    public static JSONObject getUserInfo(@RequestParam("access_token")String access_token) {
        //方法用法：携带access_token获取用户信息
        //参数集合
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("access_token",access_token));
        String s = HttpClientUtil.createGetHttp("https","gitee.com","/api/v5/user",params);
        JSONObject jsonObject = JSONObject.parseObject(s);
        return jsonObject;
    }

    @GetMapping(value = "/getemail")
    public static JSONObject getemail(@RequestParam("access_token")String access_token) {
        //方法用法：携带access_token获取用户邮箱
        //参数集合
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("access_token",access_token));
        String s = HttpClientUtil.createGetHttp("https","gitee.com","/api/v5/emails",params);

        JSONArray array =  JSONArray.parseArray(s);
        JSONObject jsonObject =  JSONObject.parseObject(array.get(0).toString());
        return jsonObject;
    }
}
