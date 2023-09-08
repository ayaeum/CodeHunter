package org.jeecg.modules.system.entity;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 *
 * @author 菜瓜皮
 * @since 2022-09-22
 */
@TableName(value = "task_path_table")
public class TaskPathTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(value = "sysUsername")
    private String sysUsername;

    @TableField(value = "taskName")
    private String taskName;

    @TableField(value = "pathSet")
    private String pathSet;

    @TableField(value = "oncheckFile")
    private String oncheckFile;

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getPathSet() {
        return pathSet;
    }

    public void setPathSet(String pathSet) {
        this.pathSet = pathSet;
    }

    public String getOncheckFile() {
        return oncheckFile;
    }

    public void setOncheckFile(String oncheckFile) {
        this.oncheckFile = oncheckFile;
    }

    @Override
    public String toString() {
        return "TaskPathTable{" +
                "sysUsername='" + sysUsername + '\'' +
                ", taskName='" + taskName + '\'' +
                ", pathSet='" + pathSet + '\'' +
                ", oncheckFile='" + oncheckFile + '\'' +
                '}';
    }

    public TaskPathTable(){
        super();
    }

    public TaskPathTable(String sysUsername, String taskName, String pathSet, String oncheckFile) {
        this.sysUsername = sysUsername;
        this.taskName = taskName;
        this.pathSet = pathSet;
        this.oncheckFile = oncheckFile;
    }

    public TaskPathTable(String sysUsername, String taskName) {
        this.sysUsername = sysUsername;
        this.taskName = taskName;
        this.pathSet = new String("[]");
        this.oncheckFile = new String("[]");
    }
}
