package org.jeecg.modules.system.controller.GitBypwd;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.entity.CertificationManagementForm;
import org.jeecg.modules.system.mapper.AppUserInfoMapper;
import org.jeecg.modules.system.mapper.CertificationManagementFormMapper;
import org.jeecg.modules.system.model.HttpClientUtil;
import org.jeecg.modules.system.service.ICertificationManagementFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.jeecg.modules.system.util.RSAutil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 功能描述
 *
 * @author: caiguapi
 * @date: 2022年08月15日 20:58
 */
@RestController
@RequestMapping("/GitByPwd")
@Slf4j
public class connect {

    private static String AUTHENTICATION_SUCCEEDED = "认证成功";
    private static String AUTHENTICATION_FAILED = "认证失败";

    @Autowired
    private AppUserInfoMapper appUserInfoMapper;

    @Autowired
    private ICertificationManagementFormService iCertificationManagementFormService;

    @Autowired
    private CertificationManagementFormMapper certificationManagementFormMapper;


    @GetMapping(value = "/gitCer")
    public static Result<JSONObject> gitCer(@RequestParam("jeecg_account")String jeecg_account,@RequestParam("account")String account,@RequestParam("password")String password,@RequestParam("Client_ID")String Client_ID,@RequestParam("Client_Secret")String Client_Secret) {
        Result<JSONObject> result1 = new Result<JSONObject>();
        String Decrypt_password = "";

        try {
            Decrypt_password = RSAutil.decrypt(password,RSAutil.PrivateKey);
            System.out.println(RSAutil.decrypt(password,RSAutil.PrivateKey));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //向码云服务器发送认证请求
        List<NameValuePair> params2 = new ArrayList<>();
        params2.add(new BasicNameValuePair("grant_type","password"));
        params2.add(new BasicNameValuePair("username",account));
        params2.add(new BasicNameValuePair("password",Decrypt_password));
        params2.add(new BasicNameValuePair("client_id",Client_ID));
        params2.add(new BasicNameValuePair("scope","projects user_info issues notes"));
        params2.add(new BasicNameValuePair("client_secret",Client_Secret));

        //创建Post请求
        JSONObject jsonObject = HttpClientUtil.createPostHttp("https","gitee.com","/oauth/token",params2);

        if(jsonObject.getString("error")!=null){
            result1.setCode(200);
            result1.setMessage("登录失败,请检查Gitee账号与密码是否正确");
            try {
//                System.out.println(RSAutil.encrypt("登录失败,请检查Gitee账号与密码是否正确",RSAutil.PublicKey));
                result1.setResult(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            result1.setCode(200);
            result1.setMessage("登录成功");
            result1.setResult(jsonObject);
        }
        return result1;
    }

    /**
     * 携带系统用户名和码云邮箱更新码云认证
     * @param jeecg_account
     * @return
     */
    @GetMapping(value = "/checkAccessToken")
    public boolean checkAccessToken(@RequestParam("jeecg_account")String jeecg_account,@RequestParam("giteeAccount")String giteeAccount) {
        QueryWrapper<CertificationManagementForm> qw = new QueryWrapper<CertificationManagementForm>();
        qw.eq("sysUsername",jeecg_account).eq("giteeAccount",giteeAccount);
        CertificationManagementForm queryresult = certificationManagementFormMapper.selectOne(qw);
        System.out.println(queryresult);

        Long nowtime = new Date().getTime();
        if(nowtime-queryresult.getDatatime()>86400000) {
            JSONObject jsonObject1 = updateAccessToken(queryresult);

            //把授权码更新到数据库
            queryresult.setAccessToken(jsonObject1.getString("access_token"));
            queryresult.setDatatime(new Date().getTime());
            certificationManagementFormMapper.update(queryresult,null);
        }
        return true;
    }

    @GetMapping(value = "/updateAccessToken")
    public static JSONObject updateAccessToken(@RequestParam("certificationManagementForm")CertificationManagementForm certificationManagementForm) {
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
