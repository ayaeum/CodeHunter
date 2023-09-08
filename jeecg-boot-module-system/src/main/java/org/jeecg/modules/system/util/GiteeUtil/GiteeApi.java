package org.jeecg.modules.system.util.GiteeUtil;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.entity.CertificationManagementForm;
import org.jeecg.modules.system.model.HttpClientUtil;
import org.jeecg.modules.system.util.RSAutil;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2022年09月15日 12:01
 */
public class GiteeApi {

    /**
     * 传入一个认证记录返回一个新的授权码
     * @param certificationManagementForm
     * @return
     */
    public static JSONObject updateAccessToken(CertificationManagementForm certificationManagementForm) {
        System.out.println(certificationManagementForm);
        Result<JSONObject> result = new Result<JSONObject>();

        //向码云服务器发送认证请求
        List<NameValuePair> params2 = new ArrayList<>();
        params2.add(new BasicNameValuePair("grant_type", "password"));
        params2.add(new BasicNameValuePair("username", certificationManagementForm.getGiteeAccount()));
        params2.add(new BasicNameValuePair("password", RSAutil.decrypt(certificationManagementForm.getGiteePassword(),RSAutil.PrivateKey)));
        params2.add(new BasicNameValuePair("client_id", certificationManagementForm.getClientId()));
        params2.add(new BasicNameValuePair("scope", "projects user_info issues notes emails"));
        params2.add(new BasicNameValuePair("client_secret", certificationManagementForm.getClientSecret()));

        //创建Post请求
        JSONObject jsonObject1 = HttpClientUtil.createPostHttp("https", "gitee.com", "/oauth/token", params2);

        return jsonObject1;
    }
}
