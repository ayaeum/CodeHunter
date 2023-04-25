package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author 菜瓜皮
 * @since 2023-02-25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("task_scan_result")
public class TaskScanResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    private Integer id;

    /**
     * 扫描时间
     */
    private LocalDateTime timestamp;

    /**
     * java规约检测结果
     */
    private String javaruleresult;

    /**
     * 克隆检测结果
     */
    private String cloneresult;

    /**
     * 缺陷检测结果
     */
    private String defectresult;

    /**
     * sql漏洞检测结果
     */
    private String sqlresult;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public String getJavaruleresult() {
        return javaruleresult;
    }

    public void setJavaruleresult(String javaruleresult) {
        this.javaruleresult = javaruleresult;
    }
    public String getCloneresult() {
        return cloneresult;
    }

    public void setCloneresult(String cloneresult) {
        this.cloneresult = cloneresult;
    }
    public String getDefectresult() {
        return defectresult;
    }

    public void setDefectresult(String defectresult) {
        this.defectresult = defectresult;
    }
    public String getSqlresult() {
        return sqlresult;
    }

    public void setSqlresult(String sqlresult) {
        this.sqlresult = sqlresult;
    }

    @Override
    public String toString() {
        return "TaskScanResult{" +
            "id=" + id +
            ", timestamp=" + timestamp +
            ", javaruleresult=" + javaruleresult +
            ", cloneresult=" + cloneresult +
            ", defectresult=" + defectresult +
            ", sqlresult=" + sqlresult +
        "}";
    }

    public TaskScanResult(Integer id, LocalDateTime timestamp, String javaruleresult) {
        this.id = id;
        this.timestamp = timestamp;
        this.javaruleresult = javaruleresult;
    }

    public TaskScanResult(Integer id, LocalDateTime timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }
}
