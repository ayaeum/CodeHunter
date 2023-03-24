package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;


/**
 * @description 用户信息
 * @author caiguapi
 * @date 2022-08-20
 */
@Data
@TableName("access_token_list")
public class AccessToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 用户账号
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 授权码
     */
    private String accessToken;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 加密密码
     */
    private String userPassword;

    /**
     * client_id
     */
    private String clientId;

    /**
     * client_secret
     */
    private String clientSecret;

    public AccessToken() {}
}
