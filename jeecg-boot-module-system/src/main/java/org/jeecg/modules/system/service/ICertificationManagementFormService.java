package org.jeecg.modules.system.service;

import org.jeecg.modules.system.entity.CertificationManagementForm;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 菜瓜皮
 * @since 2022-08-29
 */
@Service
public interface ICertificationManagementFormService extends IService<CertificationManagementForm> {

    /**
     * 直接插入认证对象
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
    public boolean DirectInsertCer(String jeecg_account, String giteeAccount, String giteePassword, String clientId, String clientSecret, String remarks, String identificationName, String accessToken, Long datatime);


    /**
     * 无重复插入认证对象
     * @param certificationManagementForm 认证主体对象
     * @return 若认证已存在，返回false.否则返回true
     */
    public boolean InsertCer(CertificationManagementForm certificationManagementForm);

    /**
     * 查询认证信息是否存在
     * @param certificationManagementForm 认证主体对象
     * @return 若认证已存在，返回true.否则返回false
     */
    public boolean Cer_Exist(CertificationManagementForm certificationManagementForm);

    /**
     * 检查认证标识名是否存在
     * @param certificationManagementForm 认证主体对象
     * @return 若认证标识名已存在，返回true.否则返回false
     */
    public boolean IdentificationName_Exist(CertificationManagementForm certificationManagementForm);

    /**
     * 更新redis缓存
     * @param Sysname 认证主体对象
     * @return 布尔值
     */
    public boolean Update_Redis(String Sysname);

    /**
     * 返回系统用户所有认证
     * @param jeecg_account 系统用户名
     * @return 认证列表
     */
    public List<CertificationManagementForm> queryCerBySysUserName(String jeecg_account);


    /**
     * 携带系统用户名和码云邮箱查询认证信息
     * @param jeecg_account 系统用户名
     * @param giteeAccount 码云邮箱
     * @return 认证记录
     */
    public boolean queryCerIsExistBySysnameandGitee(String jeecg_account,String giteeAccount);


    /**
     * 携带系统用户名和主体标识名查询授权码(带更新授权码)
     * @param jeecg_account 系统用户名
     * @param identificationName 标识名
     * @return 认证记录
     */
    public String queryCerAccessTokenBySysnameandIdentificationName(String jeecg_account,String identificationName);


    /**
     * 携带系统用户名检查标识名是否占用
     * @param jeecg_account 系统用户名
     * @param identificationName 标识名
     * @return boolean
     */
    public boolean queryCerIsExistBySysnameandIdentificationName(String jeecg_account,String identificationName);

    /**
     * 查询单个认证主体（带更新授权码）
     * @param jeecg_account
     * @param identificationName
     * @return 主体信息
     */
    public CertificationManagementForm selectOneCer(String jeecg_account,String identificationName);


    /**
     * 携带系统用户名和码云邮箱删除认证
     * @param jeecg_account 系统用户名
     * @param giteeAccount 码云邮箱
     * @return boolean
     */
    public boolean DeleteCer(String jeecg_account,String giteeAccount);

}
