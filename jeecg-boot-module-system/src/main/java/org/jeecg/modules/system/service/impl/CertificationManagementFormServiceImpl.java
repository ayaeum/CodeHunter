package org.jeecg.modules.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.C;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.entity.AppUserInfo;
import org.jeecg.modules.system.entity.CertificationManagementForm;
import org.jeecg.modules.system.mapper.CertificationManagementFormMapper;
import org.jeecg.modules.system.service.ICertificationManagementFormService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.system.util.GiteeUtil.GiteeApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jeecg.modules.system.controller.GitBypwd.connect;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 菜瓜皮
 * @since 2022-08-29
 */
@Service
public class CertificationManagementFormServiceImpl extends ServiceImpl<CertificationManagementFormMapper, CertificationManagementForm> implements ICertificationManagementFormService {

    @Resource
    private CertificationManagementFormMapper certificationManagementFormMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 无重复插入认证对象
     * @param certificationManagementForm 认证主体对象
     * @return 若认证已存在，返回false.否则返回true
     */
    public boolean InsertCer(CertificationManagementForm certificationManagementForm){
        if(Cer_Exist(certificationManagementForm)){
            return false;
        }
        certificationManagementFormMapper.insert(certificationManagementForm);
        return true;
    }

    /**
     * 查询认证信息是否存在
     * @param certificationManagementForm 认证主体对象
     * @return 若认证已存在，返回false.否则返回true
     */
    public boolean Cer_Exist(CertificationManagementForm certificationManagementForm){
        QueryWrapper<CertificationManagementForm> qw = new QueryWrapper<CertificationManagementForm>();
        qw.eq("sysUsername",certificationManagementForm.getSysUsername()).eq("giteeAccount",certificationManagementForm.getGiteeAccount()).eq("platform",certificationManagementForm.getPlatform());
        return certificationManagementFormMapper.exists(qw);
    }

    /**
     * 检查认证标识名是否存在
     * @param certificationManagementForm 认证主体对象
     * @return 若认证标识名已存在，返回true.否则返回false
     */
    public boolean IdentificationName_Exist(CertificationManagementForm certificationManagementForm){
        QueryWrapper<CertificationManagementForm> qw = new QueryWrapper<CertificationManagementForm>();
        qw.eq("sysUsername",certificationManagementForm.getSysUsername()).eq("IdentificationName",certificationManagementForm.getIdentificationName());
        return certificationManagementFormMapper.exists(qw);
    }

    /**
     * 更新redis缓存
     * @param Sysname 认证主体对象
     * @return 布尔值
     */
    public boolean Update_Redis(String Sysname){
        List<CertificationManagementForm> result = queryCerBySysUserName(Sysname);
        try {
            for (CertificationManagementForm json : result) {
                json.setGiteePassword(null);
                json.setAccessToken(null);
                json.setClientSecret(null);
                json.setClientId(null);
            }
            String redis_key = mapper.writeValueAsString(result);
            stringRedisTemplate.opsForValue().set("Certification:"+Sysname,redis_key,30, TimeUnit.MINUTES);
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 无重复插入认证对象
     * @param jeecg_account 系统用户名
     * @param giteeAccount 码云账号
     * @param giteePassword 码云密码
     * @param clientId *
     * @param clientSecret *
     * @param remarks 认证备注
     * @param identificationName 认证标识码
     * @param accessToken 授权码
     * @param datatime 时间戳
     * @return boolean
     */
    public boolean DirectInsertCer(String jeecg_account, String giteeAccount, String giteePassword, String clientId, String clientSecret, String remarks, String identificationName, String accessToken,Long datatime){

        CertificationManagementForm targetobj = new CertificationManagementForm();
        //构造对象
        targetobj.setSysUsername(jeecg_account);
        targetobj.setAccessToken(accessToken);
        targetobj.setClientId(clientId);
        targetobj.setClientSecret(clientSecret);
        targetobj.setDatatime(datatime);
        targetobj.setRemarks(remarks);
        targetobj.setGiteeAccount(giteeAccount);
        targetobj.setGiteePassword(giteePassword);
        targetobj.setIdentificationName(identificationName);
        certificationManagementFormMapper.insert(targetobj);

        return true;
    }

    /**
     * 返回系统用户所有认证
     * @param jeecg_account 系统用户名
     * @return 认证列表
     */
    public List<CertificationManagementForm> queryCerBySysUserName(String jeecg_account){
        QueryWrapper<CertificationManagementForm> qw = new QueryWrapper<CertificationManagementForm>();
        qw.eq("sysUsername",jeecg_account);
        return certificationManagementFormMapper.selectList(qw);
    }

    /**
     * 携带系统用户名和码云邮箱查询认证信息
     * @param jeecg_account 系统用户名
     * @param giteeAccount 码云邮箱
     * @return boolean
     */
    public boolean queryCerIsExistBySysnameandGitee(String jeecg_account,String giteeAccount){
        QueryWrapper<CertificationManagementForm> qw = new QueryWrapper<CertificationManagementForm>();
        qw.eq("sysUsername",jeecg_account).eq("giteeAccount",giteeAccount);
        List<CertificationManagementForm> queryresult = certificationManagementFormMapper.selectList(qw);
        return queryresult.size() != 0;
    }

    /**
     * 携带系统用户名和主体标识名查询授权码(带更新授权码)
     * @param jeecg_account 系统用户名
     * @param identificationName 标识名
     * @return 认证记录
     */
    public String queryCerAccessTokenBySysnameandIdentificationName(String jeecg_account,String identificationName){
        QueryWrapper<CertificationManagementForm> qw = new QueryWrapper<CertificationManagementForm>();
        qw.eq("sysUsername",jeecg_account).eq("identificationName",identificationName);
        CertificationManagementForm queryresult = certificationManagementFormMapper.selectOne(qw);

        Long nowtime = new Date().getTime();
        if(nowtime-queryresult.getDatatime()>86400000) {
            JSONObject jsonObject1 = GiteeApi.updateAccessToken(queryresult);

            //把授权码更新到数据库
            queryresult.setAccessToken(jsonObject1.getString("access_token"));
            queryresult.setDatatime(new Date().getTime());
            certificationManagementFormMapper.update(queryresult,null);
        }
        return queryresult.getAccessToken();
    }


    /**
     * 携带系统用户名检查标识名是否占用
     * @param jeecg_account 系统用户名
     * @param identificationName 标识名
     * @return boolean
     */
    public boolean queryCerIsExistBySysnameandIdentificationName(String jeecg_account,String identificationName){
        QueryWrapper<CertificationManagementForm> qw = new QueryWrapper<CertificationManagementForm>();
        qw.eq("sysUsername",jeecg_account).eq("identificationName",identificationName);
        List<CertificationManagementForm> queryresult = certificationManagementFormMapper.selectList(qw);
        return queryresult.size() != 0;
    }

    /**
     * 查询单个认证主体（带更新授权码）
     * @param jeecg_account
     * @param identificationName
     * @return 主体信息
     */
    public CertificationManagementForm selectOneCer(String jeecg_account,String identificationName){
        QueryWrapper<CertificationManagementForm> qw = new QueryWrapper<CertificationManagementForm>();
        qw.eq("sysUsername",jeecg_account).eq("identificationName",identificationName);
        CertificationManagementForm queryresult = certificationManagementFormMapper.selectOne(qw);

        if(queryresult.getPlatform().equals("Gitee")){
            Long nowtime = new Date().getTime();
            if(nowtime-queryresult.getDatatime()>86400000) {
//                System.out.println("密码过期");
                JSONObject jsonObject1 = GiteeApi.updateAccessToken(queryresult);
//                System.out.println("密码认证结果：");
//                System.out.println(jsonObject1);

                //把授权码更新到数据库
                queryresult.setAccessToken(jsonObject1.getString("access_token"));
                queryresult.setDatatime(new Date().getTime());
                certificationManagementFormMapper.update(queryresult,qw);
            }
        }

        return queryresult;
    }



    /**
     * 携带系统用户名和码云邮箱删除认证
     * @param jeecg_account 系统用户名
     * @param giteeAccount 码云邮箱
     * @return boolean
     */
    public boolean DeleteCer(String jeecg_account,String giteeAccount){
        QueryWrapper<CertificationManagementForm> qw = new QueryWrapper<CertificationManagementForm>();
        qw.eq("sysUsername",jeecg_account).eq("giteeAccount",giteeAccount);
        certificationManagementFormMapper.delete(qw);
        return true;
    }
}
