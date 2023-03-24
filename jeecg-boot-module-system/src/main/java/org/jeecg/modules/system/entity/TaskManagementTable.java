package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 菜瓜皮
 * @since 2022-08-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("task_management_table")
public class TaskManagementTable implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属系统用户
     */
    @TableField(value = "sysUsername")
    private String sysUsername;

    /**
     * 任务名称
     */
    @TableField(value = "taskName")
    private String taskName;

    /**
     * 所属码云账号
     */
    @TableField(value = "owner")
    private String owner;

    /**
     * 仓库
     */
    @TableField(value = "wareHouse")
    private String wareHouse;

    /**
     * 分支
     */
    @TableField(value = "branch")
    private String branch;

    /**
     * 语言
     */
    @TableField(value = "language")
    private String language;

    /**
     * 任务备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 仓库路径
     */
    @TableField(value = "path")
    private String path;

    /**
     * 仓库拥有者
     */
    @TableField(value = "repoowner")
    private String repoowner;

    /**
     * 扫描方案
     */
    @TableField(value = "scanningScheme")
    private String scanningScheme;

    /**
     * 仓库id
     */
    @TableField(value = "repoid")
    private String repoid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getSysUsername() {
        return sysUsername;
    }

    public void setSysUsername(String sysUsername) {
        this.sysUsername = sysUsername;
    }
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public String getowner() {
        return owner;
    }

    public void setowner(String giteeAccount) {
        this.owner = giteeAccount;
    }
    public String getWareHouse() {
        return wareHouse;
    }

    public void setWareHouse(String wareHouse) {
        this.wareHouse = wareHouse;
    }
    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getScanningScheme() {
        return scanningScheme;
    }

    public void setScanningScheme(String scanningScheme) {
        this.scanningScheme = scanningScheme;
    }
    public String getlanguage() {
        return language;
    }

    public void setlanguage(String language) {
        this.language = language;
    }
    public String getpath() {
        return path;
    }

    public void setpath(String path) { this.path = path; }
    public String getrepoowner() {
        return repoowner;
    }

    public void setrepoowner(String repoowner) {
        this.repoowner = repoowner;
    }
    public String getRepoid() {
        return repoid;
    }

    public void setRepoid(String repoid) {
        this.repoid = repoid;
    }

    @Override
    public String toString() {
        return "TaskManagementTable{" +
                "id=" + id +
                ", sysUsername='" + sysUsername + '\'' +
                ", taskName='" + taskName + '\'' +
                ", owner='" + owner + '\'' +
                ", wareHouse='" + wareHouse + '\'' +
                ", branch='" + branch + '\'' +
                ", language='" + language + '\'' +
                ", remark='" + remark + '\'' +
                ", path='" + path + '\'' +
                ", repoowner='" + repoowner + '\'' +
                ", scanningScheme='" + scanningScheme + '\'' +
                ", repoid='" + repoid + '\'' +
                '}';
    }

    public TaskManagementTable(String sysUsername, String taskName, String owner, String wareHouse, String branch, String language, String remark, String path, String repoowner, String scanningScheme) {
        this.sysUsername = sysUsername;
        this.taskName = taskName;
        this.owner = owner;
        this.wareHouse = wareHouse;
        this.branch = branch;
        this.language = language;
        this.remark = remark;
        this.path = path;
        this.repoowner = repoowner;
        this.scanningScheme = scanningScheme;
    }
}
