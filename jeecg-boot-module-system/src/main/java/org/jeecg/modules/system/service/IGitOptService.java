package org.jeecg.modules.system.service;


import org.jeecg.modules.system.entity.CertificationManagementForm;
import org.jeecg.modules.system.entity.TaskManagementTable;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface IGitOptService {

    /**
     * 认证GitHub账号
     * @return
     */
    public boolean GitHubConnect();

    public JSONArray getScanObj(String[] filelist, CertificationManagementForm certificationManagementForm,TaskManagementTable taskManagementTable);

}
