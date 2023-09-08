package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 菜瓜皮
 * @since 2022-08-29
 */
@TableName("certification_management_form")
public class CertificationManagementForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @TableField(fill = FieldFill.INSERT)
    private Long id;

    /**
     * 标识名称
     */
    @TableField(value = "identificationName")
    private String identificationName;

    /**
     * 所属系统用户
     */
    @TableField(value = "sysUsername")
    private String sysUsername;

    /**
     * 码云账户
     */
    @TableField(value = "giteeAccount")
    private String giteeAccount;

    /**
     * 码云加密密码
     */
    @TableField(value = "giteePassword")
    private String giteePassword;

    /**
     * 验证id
     */
    @TableField(value = "clientId")
    private String clientId;

    /**
     * 验证秘钥
     */
    @TableField(value = "clientSecret")
    private String clientSecret;

    /**
     * 授权码
     */
    @TableField(value = "accessToken")
    private String accessToken;

    /**
     * 时间戳
     */
    @TableField(value = "datatime")
    private Long datatime;

    /**
     * 备注
     */
    @TableField(value = "remarks")
    private String remarks;

    /**
     * 平台
     */
    @TableField(value = "platform")
    private String platform;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getIdentificationName() {
        return identificationName;
    }

    public void setIdentificationName(String identificationName) {
        this.identificationName = identificationName;
    }
    public String getSysUsername() {
        return sysUsername;
    }

    public void setSysUsername(String sysUsername) {
        this.sysUsername = sysUsername;
    }
    public String getGiteeAccount() {
        return giteeAccount;
    }

    public void setGiteeAccount(String giteeAccount) {
        this.giteeAccount = giteeAccount;
    }
    public String getGiteePassword() {
        return giteePassword;
    }

    public void setGiteePassword(String giteePassword) {
        this.giteePassword = giteePassword;
    }
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public Long getDatatime() {
        return datatime;
    }

    public void setDatatime(Long datatime) {
        this.datatime = datatime;
    }
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return "CertificationManagementForm{" +
                "id=" + id +
                ", identificationName='" + identificationName + '\'' +
                ", sysUsername='" + sysUsername + '\'' +
                ", giteeAccount='" + giteeAccount + '\'' +
                ", giteePassword='" + giteePassword + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", datatime=" + datatime +
                ", remarks='" + remarks + '\'' +
                ", platform='" + platform + '\'' +
                '}';
    }
}
