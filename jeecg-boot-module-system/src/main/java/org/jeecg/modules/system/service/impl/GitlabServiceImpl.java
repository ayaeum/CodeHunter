package org.jeecg.modules.system.service.impl;

import lombok.val;
import org.jeecg.modules.system.model.HttpClientUtil;
import org.jeecg.modules.system.service.IGitlabService;
import org.springframework.stereotype.Service;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2022年10月18日 21:50
 */
@Service
public class GitlabServiceImpl implements IGitlabService {

    /**
     * 获取仓库某分支文件树目录
     * @param host
     * @param repository_path
     * @param ref
     * @param private_token
     * @return
     */
    public String getFileTree(String host,String repository_path,String ref,String private_token){
        String baseurl = "http://"+host+"/api/v4/projects/"+repository_path+"/repository/tree?private_token="+private_token+"&recursive=true"+"&ref="+ref;
        return HttpClientUtil.createGetHttpByPath(baseurl);
    }

    /**
     * 获取某个仓库的分支信息
     * @param host gitlab地址
     * @param id 项目id
     * @param private_token 访问令牌
     * @return
     */
    public String getRepoBranchs(String host,String id,String private_token){
        return HttpClientUtil.createGetHttpByPath("http://"+host+"/api/v4/projects/"+id+"/repository/branches?private_token="+private_token);
    }

    /**
     * 获取文件内容
     * @param host
     * @param repository_path
     * @param file_path
     * @param ref
     * @param private_token
     */
    public String getFileContext(String host,String repository_path,String file_path,String ref,String private_token){
        String url = "http://"+host+"/api/v4/projects/"+repository_path+"/repository/files/"+file_path+"/raw?private_token="+private_token+"&ref="+ref;
        return HttpClientUtil.createGetHttpByPath(url);
    }

    /**
     * 获取用户所有项目
     * @param host
     * @param private_token
     * @return
     */
    public String getUserProjects(String host,String private_token){
        return HttpClientUtil.createGetHttpByPath("http://"+host+"/api/v4/projects?private_token="+private_token);
    }

    /**
     * 验证GitLab账户
     * @param host
     * @param private_token
     * @return 认证状态
     */
    public boolean getGitLabConnect(String host,String private_token){
        System.out.println(host+":"+private_token);
        String getHttpByPath = HttpClientUtil.getGetState("http://" + host + "/api/v4/projects?private_token=" + private_token);
        System.out.println(getHttpByPath);
        if(getHttpByPath.contains("200")){
            return true;
        }
        return false;
    }
}
