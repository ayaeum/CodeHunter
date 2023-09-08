package org.jeecg.modules.system.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * @description 用户信息
 * @author caiguapi
 * @date 2022-08-20
 */
@Data
@TableName("app_user_info")
public class AppUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 用户账号
     */
    private String id;

    /**
     * 用户账号
     */
    private String userid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 授权码
     */
    private String accesstoken;

    /**
     * 创建时间
     */
    private Long datatime;

    /**
     * 用户邮箱
     */
    private String giteeaccount;

    /**
     * 加密密码
     */
    private String giteepassword;

    /**
     * client_id
     */
    private String clientid;

    /**
     * client_secret
     */
    private String clientsecret;

    public AppUserInfo() {}
}
