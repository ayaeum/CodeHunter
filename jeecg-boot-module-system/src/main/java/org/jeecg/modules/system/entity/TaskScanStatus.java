package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 菜瓜皮
 * @since 2023-02-18
 */
@TableName("task_scan_status")
@AllArgsConstructor
public class TaskScanStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 扫描状态
     */
    private String status;

    /**
     * 扫描进度
     */
    private String process;

    /**
     * 克隆扫描进度
     */
    private String process1;

    /**
     * 扫描信息
     */
    private String scanmsg;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
    public String getScanmsg() {
        return scanmsg;
    }

    public void setScanmsg(String scanmsg) {
        this.scanmsg = scanmsg;
    }
    public String getProcess1() {
        return process1;
    }

    public void setProcess1(String process1) {
        this.process1 = process1;
    }

    @Override
    public String toString() {
        return "TaskScanStatus{" +
            "id=" + id +
            ", status=" + status +
            ", process=" + process +
            ", scanmsg=" + scanmsg +
        "}";
    }

    public TaskScanStatus(){
        super();
    }

    public TaskScanStatus(Integer id) {
        this.id = id;
        this.process="0";
        this.process1="0";
        this.scanmsg="开始扫描\n";
        this.status="正在拉取任务代码....\n";
    }
}
