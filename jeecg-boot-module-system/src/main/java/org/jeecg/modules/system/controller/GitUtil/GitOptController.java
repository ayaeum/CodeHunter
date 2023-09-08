package org.jeecg.modules.system.controller.GitUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jeecg.modules.system.entity.CertificationManagementForm;
import org.jeecg.modules.system.model.HttpClientUtil;
import org.jeecg.modules.system.service.ICertificationManagementFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述
 *
 * @author: caiguapi
 * @date: 2023年02月08日 14:15
 */
@RestController
@RequestMapping("/GitOptController")
@Slf4j
public class GitOptController {

    @Autowired
    private ICertificationManagementFormService iCertificationManagementFormService;

    /**
     * 获取仓库文件列表
     * @param task 任务信息
     * @return 任务列表
     */
    @GetMapping(value = "/getFileList")
    public JSONObject getFileList(@RequestParam("task")String task) {
        JSONObject taskJSON=(JSONObject) JSONObject.parse(task);
        //首先找出主体，并判断属于哪个平台()
        CertificationManagementForm cer=iCertificationManagementFormService.selectOneCer(taskJSON.getString("sysUsername"),taskJSON.getString("owner"));
        //分别根据不同的平台去请求文件列表
        JSONObject jsonObject = null;
        //参数集合
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("recursive","1"));
        params.add(new BasicNameValuePair("access_token",cer.getAccessToken()));
        String s;
        switch (cer.getPlatform()){
            case "Gitee":
                s = HttpClientUtil.createGetHttp("https","gitee.com","/api/v5/repos/"+taskJSON.getString("repoowner")+"/"+taskJSON.getString("path")+"/git/trees/"+taskJSON.getString("branch"),params);
                jsonObject = JSONObject.parseObject(s);
                break;
            case "GitLab":
                //GitLab平台的地址不固定,需要获取地址，还需要拿到仓库的id
                params.add(new BasicNameValuePair("ref",taskJSON.getString("branch")));
                s = HttpClientUtil.createGetHttp("http",cer.getGiteeAccount(),"/api/v4/projects/"+taskJSON.getString("repoid")+"/repository/tree",params);
                jsonObject=new JSONObject();
                jsonObject.put("tree", JSONArray.parseArray(s));
                break;
            case "GitHub":
                break;
        }
        return jsonObject;
    }

    /**
     * 获取账号仓库与分支信息
     * @param task 任务信息
     * @return 任务列表
     */
    @GetMapping(value = "/getReposAndBranches")
    public static String getReposAndBranches(@RequestParam("task")String task) {
        JSONObject taskJSON=(JSONObject) JSONObject.parse(task);
        //首先找出主体，并判断属于哪个平台
        return "s";
    }
}
