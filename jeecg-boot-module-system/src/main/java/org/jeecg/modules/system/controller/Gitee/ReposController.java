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

import java.util.*;

import org.jeecg.modules.system.model.Base64Decode;

/**
 * 功能描述
 *
 * @author: 周泽龙
 * @date: 2022年08月06日 21:03
 */
@RestController
@RequestMapping("/Git")
@Slf4j
public class ReposController {

    @GetMapping(value = "/getRepos")
    public static JSONObject getRepos(@RequestParam("access_token")String access_token) {
        //方法用法：携带access_token获取授权用户信息

        //参数集合
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("access_token",access_token));
        String s = HttpClientUtil.createGetHttp("https","gitee.com","/api/v5/user",params);
        JSONObject jsonObject = JSONObject.parseObject(s);
        System.out.println(jsonObject.getString("repos_url"));
        return jsonObject;
    }

    @GetMapping(value = "/getReposAllBranches")
    public static JSONObject getReposAllBranches(@RequestParam("access_token")String access_token,@RequestParam("owner")String owner,@RequestParam("repo")String repo) {
        //参数集合
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("access_token",access_token));
        String s = HttpClientUtil.createGetHttp("https","gitee.com","/api/v5/repos/"+owner+"/"+repo+"/branches",params);
        JSONArray array =  JSONArray.parseArray(s);
        JSONObject jsonObject =  JSONObject.parseObject(array.get(0).toString());

        Iterator iterator = array.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }
        return jsonObject;
    }

    @GetMapping(value = "/getReposBranches")
    public static JSONObject getReposBranches(@RequestParam("access_token")String access_token,@RequestParam("owner")String owner,@RequestParam("repo")String repo,@RequestParam("branch")String branch) {

        //参数集合
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("access_token",access_token));
        String s = HttpClientUtil.createGetHttp("https","gitee.com","/api/v5/repos/"+owner+"/"+repo+"/branches/"+branch,params);

        JSONObject jsonObject = JSONObject.parseObject(s);
        System.out.println(jsonObject);
        return jsonObject;
    }

    /**
     * 获取某个仓库某个分支下某路径的代码
     * @param access_token
     * @param owner
     * @param repo
     * @param path
     * @return
     */
    @GetMapping(value = "/getPathContents")
    public static JSONObject getPathContents(@RequestParam("access_token")String access_token,@RequestParam("owner")String owner,@RequestParam("repo")String repo,@RequestParam("path")String path) {

        //参数集合
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("access_token",access_token));
        String s = HttpClientUtil.createGetHttp("https","gitee.com","/api/v5/repos/"+owner+"/"+repo+"/contents/"+path,params);

        JSONObject jsonObject = JSONObject.parseObject(s);
        System.out.println(jsonObject.getString("name"));
//        System.out.println("解码后：");
//        System.out.println(Base64Decode.decode(jsonObject.getString("content")));
//        System.out.println((String)Base64Decode.decode(str));
        return jsonObject;
    }
}
