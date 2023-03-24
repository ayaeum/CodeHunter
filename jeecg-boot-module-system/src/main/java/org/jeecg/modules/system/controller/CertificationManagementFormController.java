package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.controller.Gitee.gitController;
import org.jeecg.modules.system.entity.CertificationManagementForm;
import org.jeecg.modules.system.entity.MailTemplate;
import org.jeecg.modules.system.model.HttpClientUtil;
import org.jeecg.modules.system.service.ICertificationManagementFormService;
import org.jeecg.modules.system.service.IGitlabService;
import org.jeecg.modules.system.service.ISendMailService;
import org.jeecg.modules.system.util.RSAutil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.jeecg.modules.system.controller.Test.Enum;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jeecg.modules.system.controller.GitBypwd.connect;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 菜瓜皮
 * @since 2022-08-29
 */
@Slf4j
@RestController
@RequestMapping("/certificationManagementForm")
public class CertificationManagementFormController {

    @Autowired
    private ICertificationManagementFormService iCertificationManagementFormService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ISendMailService iSendMailService;

    @Autowired
    private IGitlabService iGitlabService;

    /**
     * 新增认证，若存在不予添加，不存在则加之
     * @param certificationManagementForm
     * @return
     */
    @GetMapping(value = "/addCertification")
    public Result<List<CertificationManagementForm>> addCertification(CertificationManagementForm certificationManagementForm){
        Result<List<CertificationManagementForm>> resultobj = new Result<List<CertificationManagementForm>>();
        //1.首先判断认证是否存在(根据系统账号、平台、账号、标识名)
        if(iCertificationManagementFormService.Cer_Exist(certificationManagementForm)){
            resultobj.setMessage("认证重复");
            return resultobj;
        }

        if(iCertificationManagementFormService.IdentificationName_Exist(certificationManagementForm)){
            resultobj.setMessage("认证标识名已存在");
            return resultobj;
        }
        certificationManagementForm.setDatatime((new Date().getTime())/1000);
        switch (certificationManagementForm.getPlatform()){
            case "GitLab":
                if(iGitlabService.getGitLabConnect(certificationManagementForm.getGiteeAccount(), RSAutil.decrypt(certificationManagementForm.getGiteePassword(), RSAutil.PrivateKey))){
                    resultobj.setMessage(Enum.ADD_SUCCESS);
                    certificationManagementForm.setAccessToken(RSAutil.decrypt(certificationManagementForm.getGiteePassword(), RSAutil.PrivateKey));
                    iCertificationManagementFormService.InsertCer(certificationManagementForm);
                    resultobj.setResult(iCertificationManagementFormService.queryCerBySysUserName(certificationManagementForm.getSysUsername()));
                }
                break;
            case "Gitee":
                Result<JSONObject> result = connect.gitCer(certificationManagementForm.getSysUsername(),certificationManagementForm.getGiteeAccount(),certificationManagementForm.getGiteePassword(),certificationManagementForm.getClientId(),certificationManagementForm.getClientSecret());
                if(result.getMessage().equals(Enum.GITEE_SIGNIN_SUCCESS)){
                    iSendMailService.SendMail(new MailTemplate("1192129669@qq.com",certificationManagementForm.getGiteeAccount(),"CodeHunter邮件通知","尊敬的用户，您的码云账号"+certificationManagementForm.getGiteeAccount()+"已授权CodeHunter平台，感谢使用CodeHunter平台。如非本人操作，请立即更改密码。"));
                    certificationManagementForm.setDatatime(Long.parseLong(result.getResult().getString("created_at")));
                    certificationManagementForm.setAccessToken(result.getResult().getString("access_token"));
                    iCertificationManagementFormService.InsertCer(certificationManagementForm);
                    resultobj.setCode(Enum.REQUEST_NORMAL);
                    resultobj.setMessage(Enum.ADD_SUCCESS);
                    resultobj.setResult(iCertificationManagementFormService.queryCerBySysUserName(certificationManagementForm.getSysUsername()));
                }
                break;
            case "GitHub":
                //先验证秘钥

                //若秘钥通过，则存入数据库
                System.out.println("GitHub");
                break;
        }
//        iCertificationManagementFormService.Update_Redis(certificationManagementForm.getSysUsername());
        resultobj.setMessage("添加成功");
        return resultobj;
    }

    /**
     * 根据系统用户名与码云邮箱进行认证查询
     * @param jeecg_account
     * @param giteeAccount
     * @return
     */
    @GetMapping(value = "/queryCertification")
    public Result<CertificationManagementForm> queryCertification(@RequestParam("jeecg_account")String jeecg_account, @RequestParam("giteeAccount")String giteeAccount){
        Result<CertificationManagementForm> resultobj = new Result<CertificationManagementForm>();
        QueryWrapper<CertificationManagementForm> qw = new QueryWrapper<CertificationManagementForm>();
        qw.eq("sysUsername",jeecg_account).eq("giteeAccount",giteeAccount);
        List<CertificationManagementForm> result = iCertificationManagementFormService.list(qw);

//        try {
//            String redis_key = mapper.writeValueAsString(result);
//            System.out.println(redis_key);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }

        if(result.size()==0){
            resultobj.setCode(Enum.REQUEST_NORMAL);
            resultobj.setMessage(Enum.QUERY_NULL);
            resultobj.setResult(null);
        }
        else{
            resultobj.setCode(Enum.REQUEST_NORMAL);
            resultobj.setMessage(Enum.AUTHENTICATION_EXIST);
            resultobj.setResult(result.get(0));
        }
        return resultobj;
    }

    @GetMapping(value = "/queryCertificationBySysName")
    public Result<List<CertificationManagementForm>> queryCertificationBySysName(@RequestParam("jeecg_account")String jeecg_account){
        Result<List<CertificationManagementForm>> resultobj = new Result<List<CertificationManagementForm>>();

        List<CertificationManagementForm> result = iCertificationManagementFormService.queryCerBySysUserName(jeecg_account);

//        if(stringRedisTemplate.opsForValue().get("Certification:"+jeecg_account) != null){
//            result = (List)JSONArray.parseArray(stringRedisTemplate.opsForValue().get("Certification:"+jeecg_account));
//        }else{
//            iCertificationManagementFormService.Update_Redis(jeecg_account);
//        }
        resultobj.setMessage(Enum.QUERY_SUCCESS);
        resultobj.setCode(Enum.REQUEST_NORMAL);
        resultobj.setResult(result);
        return resultobj;
    }


    @GetMapping(value = "/deleteCertificationBySysNameandGitee")
    public Result<List<CertificationManagementForm>> deleteCertificationBySysNameandGitee(@RequestParam("jeecg_account")String jeecg_account,@RequestParam("giteeAccount")String giteeAccount){
        Result<List<CertificationManagementForm>> resultobj = new Result<List<CertificationManagementForm>>();
        List<CertificationManagementForm> result = null;
        QueryWrapper<CertificationManagementForm> qw = new QueryWrapper<CertificationManagementForm>();
        qw.eq("sysUsername",jeecg_account).eq("giteeAccount",giteeAccount);
        if(!iCertificationManagementFormService.queryCerIsExistBySysnameandGitee(jeecg_account,giteeAccount)){
            resultobj.setCode(Enum.REQUEST_NORMAL);
            resultobj.setMessage(Enum.DELETE_FAILED+Enum.CERTIFICATION_NOT_EXIST);
            resultobj.setResult(iCertificationManagementFormService.queryCerBySysUserName(jeecg_account));
        }
        else{
            resultobj.setCode(Enum.REQUEST_NORMAL);
            resultobj.setMessage(Enum.DELETE_SUCCESS);
            iCertificationManagementFormService.DeleteCer(jeecg_account,giteeAccount);
//            iCertificationManagementFormService.Update_Redis(jeecg_account);
            resultobj.setResult(iCertificationManagementFormService.queryCerBySysUserName(jeecg_account));
        }
        return resultobj;
    }


    /**
     * 通过系统账号获取拥有的认证主体、主体仓库、仓库分支信息
     * @param jeecg_account 系统用户账号
     * @return 列表
     */
    @GetMapping(value = "/queryCerandRepoBySysName")
    public Result<List<JSONObject>> queryCerandRepoBySysName(@RequestParam("jeecg_account")String jeecg_account){

        Result<List<JSONObject>> result = new Result<List<JSONObject>>();
        List<CertificationManagementForm> CerList = iCertificationManagementFormService.queryCerBySysUserName(jeecg_account);
        List<JSONObject> list = new LinkedList<JSONObject>();

        Iterator<CertificationManagementForm> iter = CerList.listIterator();
        while(iter.hasNext()){
            JSONObject obj = new JSONObject();
            CertificationManagementForm carry = iter.next();
            obj.put("identificationName",carry.getIdentificationName());
            obj.put("platform",carry.getPlatform());

            if(carry.getPlatform().equals("Gitee")){
                //认证检查
                if(new Date().getTime()-carry.getDatatime()>86400){
                    JSONObject result2 = connect.updateAccessToken(carry);
                    if(result2.getString("error") != null){
                        result.setCode(400);
                        result.setMessage(result2.getString("error"));
                        result.setResult(null);
                    }
                    else{
                        //将认证信息更新到数据库
                        carry.setAccessToken(result2.getString("access_token"));
                        carry.setDatatime(result2.getLong("created_at"));
                        iCertificationManagementFormService.updateById(carry);
                    }
                }
                //获取仓库分支信息
                int i;
                JSONArray objrepo = gitController.getAllRepos(carry.getAccessToken());
                for(i=0;i<objrepo.size();i++) {
                    //直接拼装path请求分支信息
                    String s = HttpClientUtil.createGetHttpByPath(objrepo.getJSONObject(i).getString("branches_url").replaceAll("\\{/branch}","")+"?access_token="+carry.getAccessToken());
                    objrepo.getJSONObject(i).put("branchs",JSONArray.parseArray(s));
                }
                obj.put("repo",objrepo);
            }else{
                if(carry.getPlatform().equals("GitLab")){
                    //先拿到认证的所有仓库信息
                    String s = iGitlabService.getUserProjects(carry.getGiteeAccount(),carry.getAccessToken());
                    System.out.println(s);
                    JSONArray array = JSON.parseArray(s);
                    JSONArray gitlabrepo = new JSONArray();
                    for(int i=0;i<array.size();i++){
                        String str = iGitlabService.getRepoBranchs(carry.getGiteeAccount(),array.getJSONObject(i).getString("id"),carry.getAccessToken());
                        JSONArray strjson = JSON.parseArray(str);
                        array.getJSONObject(i).put("branchs",strjson);
                        gitlabrepo.add(array.getJSONObject(i));
                    }
                    obj.put("repo",gitlabrepo);
                }
            }
            list.add(obj);
        }
        result.setResult(list);
        return result;
    }
}
