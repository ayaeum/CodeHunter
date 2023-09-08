package org.jeecg.modules.system.service;

import org.jeecg.modules.system.model.HttpClientUtil;

public interface IGitlabService {

    /**
     * 获取仓库某分支文件树目录
     * @param host
     * @param repository_path
     * @param ref
     * @param private_token
     * @return
     */
    public String getFileTree(String host,String repository_path,String ref,String private_token);

    /**
     * 获取某个仓库的分支信息
     * @param host gitlab地址
     * @param id 项目id
     * @param private_token 访问令牌
     * @return
     */
    public String getRepoBranchs(String host,String id,String private_token);

    /**
     * 获取文件内容
     * @param host
     * @param repository_path
     * @param file_path
     * @param ref
     * @param private_token
     */
    public String getFileContext(String host,String repository_path,String file_path,String ref,String private_token);

    /**
     * 获取用户所有项目
     * @param host
     * @param private_token
     * @return
     */
    public String getUserProjects(String host,String private_token);

    /**
     * 验证GitLab账户
     * @param host
     * @param private_token
     * @return 认证状态
     */
    public boolean getGitLabConnect(String host,String private_token);
}
