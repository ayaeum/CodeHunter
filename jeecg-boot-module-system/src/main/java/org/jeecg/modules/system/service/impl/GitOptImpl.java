package org.jeecg.modules.system.service.impl;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jeecg.modules.system.entity.CertificationManagementForm;
import org.jeecg.modules.system.entity.TaskManagementTable;
import org.jeecg.modules.system.model.Base64Decode;
import org.jeecg.modules.system.model.HttpClientUtil;
import org.jeecg.modules.system.service.IGitOptService;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2023年02月13日 22:44
 */
@Service
public class GitOptImpl implements IGitOptService {

    public boolean GitHubConnect(){
        //参数集合
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("client_id",client_id));
//        params.add(new BasicNameValuePair("client_secret",client_secret));
//        s = HttpClientUtil.createGetHttp("http://api.github.com/users",cer.getGiteeAccount(),"/api/v4/projects/5/repository/tree",params);
//        System.out.println(s);
//        https:/caiguapi?=10022e4cccf989834c62&client_secret=2d8994d81e2def53ce0dd2b2157a0d52f038d072
        return true;
    }

    public JSONArray getScanObj(String[] filelist, CertificationManagementForm certificationManagementForm,TaskManagementTable taskManagementTable){
        JSONArray jsonArray=new JSONArray();

        switch (certificationManagementForm.getPlatform()){
            case "Gitee":
                for (String value : filelist) {
                    List<NameValuePair> params = new ArrayList<>();
                    JSONObject jsonObject=new JSONObject();
                    params.add(new BasicNameValuePair("access_token",certificationManagementForm.getAccessToken()));
                    String s = HttpClientUtil.createGetHttp("https","gitee.com","/api/v5/repos/"+taskManagementTable.getrepoowner()+"/"+taskManagementTable.getpath()+"/contents/"+value,params);
                    if (s.startsWith("[")){
                        continue;
                    }
                    String content=Base64Decode.decode(JSONObject.parseObject(s).getString("content"));
                    jsonObject.put("code",content);
                    jsonObject.put("filename",value);
                    jsonArray.add(jsonObject);
                }
                break;
            case "GitLab":
                for (String s : filelist) {
//                    System.out.println(s);
                }
                break;
        }
        return jsonArray;
    }
}
