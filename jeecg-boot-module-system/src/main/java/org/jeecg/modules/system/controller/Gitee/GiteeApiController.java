package org.jeecg.modules.system.controller.Gitee;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jeecg.modules.system.entity.CertificationManagementForm;
import org.jeecg.modules.system.entity.TaskManagementTable;
import org.jeecg.modules.system.mapper.CertificationManagementFormMapper;
import org.jeecg.modules.system.mapper.TaskManagementTableMapper;
import org.jeecg.modules.system.model.HttpClientUtil;
import org.jeecg.modules.system.service.ICertificationManagementFormService;
import org.jeecg.modules.system.service.IGitlabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.jeecg.modules.system.controller.GitBypwd.connect;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2022年08月29日 14:29
 */
@RestController
@RequestMapping("/GiteeApiController")
@Slf4j
public class GiteeApiController {

    @Resource
    private TaskManagementTableMapper taskManagementTableMapper;

    @Resource
    private CertificationManagementFormMapper certificationManagementFormMapper;

    @Autowired
    private IGitlabService iGitlabService;


    @GetMapping(value = "/getRepoAllbranch")
    public static JSONArray getRepoAllbranch(@RequestParam("path")String path) {
        String s = HttpClientUtil.createGetHttpByPath(path);
        return JSONArray.parseArray(s);
    }

    /**
     * 获取某个仓库某个分支的目录Tree
     * @param jeecg_account 系统用户名
     * @param taskName 任务名称
     * @param owner 仓库所属用户
     * @param repo 仓库路径
     * @param sha 分支
     * @param recursive 默认1
     * @return
     */
    @GetMapping(value = "/getTree")
    public JSONObject getTree(@RequestParam("jeecg_account")String jeecg_account,@RequestParam("taskName")String taskName,@RequestParam("owner")String owner,@RequestParam("repo")String repo,@RequestParam("sha")String sha,@RequestParam(required = false,value = "recursive",defaultValue = "1")String recursive) {
        QueryWrapper<TaskManagementTable> qw1 = new QueryWrapper<TaskManagementTable>();
        qw1.eq("sysUsername",jeecg_account).eq("taskName",taskName);
        TaskManagementTable taskManagementTable = taskManagementTableMapper.selectOne(qw1);
        QueryWrapper<CertificationManagementForm> qw2 = new QueryWrapper<CertificationManagementForm>();
        qw2.eq("sysUsername",jeecg_account).eq("identificationName",taskManagementTable.getowner());
        CertificationManagementForm qw2queryresult = certificationManagementFormMapper.selectOne(qw2);

        JSONObject jsonObject = null;
        if(qw2queryresult.getPlatform().equals("Gitee")){
            Long nowtime = new Date().getTime();
            if(nowtime-qw2queryresult.getDatatime()>86400000){
                qw2queryresult.setAccessToken(connect.updateAccessToken(qw2queryresult).getString("access_token"));
                //将授权码更新到数据库
                certificationManagementFormMapper.update(qw2queryresult,qw2);
            }

            //参数集合
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("access_token",qw2queryresult.getAccessToken()));
            params.add(new BasicNameValuePair("recursive",recursive));
            String s = HttpClientUtil.createGetHttp("https","gitee.com","/api/v5/repos/"+owner+"/"+repo+"/git/trees/"+sha,params);
            jsonObject = JSONObject.parseObject(s);
        }else{
            if(qw2queryresult.getPlatform().equals("GitLab")){
                String s = iGitlabService.getFileTree(qw2queryresult.getGiteeAccount(),"root%2F"+taskManagementTable.getpath(),taskManagementTable.getBranch(),qw2queryresult.getAccessToken());
                jsonObject = new JSONObject();
                jsonObject.put("tree",JSONArray.parseArray(s));
            }
        }
        return jsonObject;
    }
}
