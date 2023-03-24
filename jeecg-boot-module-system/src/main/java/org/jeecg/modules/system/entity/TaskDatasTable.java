package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 菜瓜皮
 * @since 2022-09-18
 */
@TableName("task_datas_table")
public class TaskDatasTable implements Serializable {

    public TaskDatasTable(String sysUsername, String taskName, Integer cloneRank, Integer clone_serious, Integer clone_warning, Integer clone_proposal) {
        this.sysUsername = sysUsername;
        this.taskName = taskName;
        this.cloneRank = cloneRank;
        this.clone_serious = clone_serious;
        this.clone_warning = clone_warning;
        this.clone_proposal = clone_proposal;
    }

    private static final long serialVersionUID = 1L;

    /**
     * 任务名称
     */
    @TableField(value = "sysUsername")
    private String sysUsername;

    /**
     * 任务名称
     */
    @TableField(value = "taskName")
    private String taskName;

    /**
     * 克隆代码记录编号
     */
    @TableField(value = "clone_rank")
    private Integer cloneRank;

    /**
     * 克隆代码严重问题个数
     */
    @TableField(value = "clone_serious")
    private Integer clone_serious;

    /**
     * 克隆代码警告问题个数
     */
    @TableField(value = "clone_warning")
    private Integer clone_warning;

    /**
     * 克隆代码建议问题个数
     */
    @TableField(value = "clone_proposal")
    private Integer clone_proposal;

    public String getsysUsername() {
        return sysUsername;
    }

    public void setsysUsername(String sysUsername) {
        this.sysUsername = sysUsername;
    }
    public String getTaskName() { return taskName; }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public Integer getCloneRank() {
        return cloneRank;
    }

    public void setCloneRank(Integer cloneRank) {
        this.cloneRank = cloneRank;
    }

    public Integer getClone_serious() {
        return clone_serious;
    }

    public void setClone_serious(Integer clone_serious) {
        this.clone_serious = clone_serious;
    }

    public Integer getClone_warning() {
        return clone_warning;
    }

    public void setClone_warning(Integer clone_warning) {
        this.clone_warning = clone_warning;
    }

    public Integer getClone_proposal() {
        return clone_proposal;
    }

    public void setClone_proposal(Integer clone_proposal) {
        this.clone_proposal = clone_proposal;
    }

    @Override
    public String toString() {
        return "TaskDatasTable{" +
                "sysUsername='" + sysUsername + '\'' +
                ", taskName='" + taskName + '\'' +
                ", cloneRank=" + cloneRank +
                ", clone_serious=" + clone_serious +
                ", clone_warning=" + clone_warning +
                ", clone_proposal=" + clone_proposal +
                '}';
    }
}
