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
 * @since 2022-09-19
 */
@TableName("task_details_table")
public class TaskDetailsTable implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 前端现实的颜色
     */
    @TableField(value = "color")
    private String color;

    /**
     * 问题描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 文件1问题结束行
     */
    @TableField(value = "endLine_1")
    private Integer endLine_1;

    /**
     * 文件2问题结束行
     */
    @TableField(value = "endLine_2")
    private Integer endLine_2;

    /**
     * 文件1路径
     */
    @TableField(value = "filePath_1")
    private String filePath_1;

    /**
     * 文件2路径
     */
    @TableField(value = "filePath_2")
    private String filePath_2;

    /**
     * 严重程度
     */
    @TableField(value = "level")
    private String level;

    /**
     * 相似度
     */
    @TableField(value = "similarity")
    private Double similarity;

    /**
     * 文件1问题开始行
     */
    @TableField(value = "startLine_1")
    private Integer startLine_1;

    /**
     * 文件1问题开始行
     */
    @TableField(value = "startLine_2")
    private Integer startLine_2;

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
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    public Integer getEndLine_1() {
        return endLine_1;
    }

    public void setEndLine_1(Integer endLine_1) {
        this.endLine_1 = endLine_1;
    }

    public Integer getEndLine_2() {
        return endLine_2;
    }

    public void setEndLine_2(Integer endLine_2) {
        this.endLine_2 = endLine_2;
    }

    public String getFilePath_1() {
        return filePath_1;
    }

    public void setFilePath_1(String filePath_1) {
        this.filePath_1 = filePath_1;
    }

    public String getFilePath_2() {
        return filePath_2;
    }

    public void setFilePath_2(String filePath_2) {
        this.filePath_2 = filePath_2;
    }

    public Integer getStartLine_1() {
        return startLine_1;
    }

    public void setStartLine_1(Integer startLine_1) {
        this.startLine_1 = startLine_1;
    }

    public Integer getStartLine_2() {
        return startLine_2;
    }

    public void setStartLine_2(Integer startLine_2) {
        this.startLine_2 = startLine_2;
    }

    public TaskDetailsTable(String sysUsername, String taskName, String color, String description, Integer endLine_1, Integer endLine_2, String filePath_1, String filePath_2, String level, Double similarity, Integer startLine_1, Integer startLine_2) {
        this.sysUsername = sysUsername;
        this.taskName = taskName;
        this.color = color;
        this.description = description;
        this.endLine_1 = endLine_1;
        this.endLine_2 = endLine_2;
        this.filePath_1 = filePath_1;
        this.filePath_2 = filePath_2;
        this.level = level;
        this.similarity = similarity;
        this.startLine_1 = startLine_1;
        this.startLine_2 = startLine_2;
    }
}
