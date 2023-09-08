package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 菜瓜皮
 * @since 2023-02-15
 */
@Data
@AllArgsConstructor
@TableName("task_scanning_scheme")
public class TaskScanningScheme implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO,value = "id")
    private Integer id;

    /**
     * java规约配置
     */
    private String javarule;

    /**
     * 缺陷规约配置
     */
    private String defectrule;

    /**
     * 近似代码配置
     */
    private String approximatecode;

    /**
     * sql规约配置
     */
    private String sqlrule;

    /**
     * 邮箱通知
     */
    private String email;

    /**
     * 自动触发扫描开关
     */
    private String automaticscan;

    /**
     * 邮箱通知开关
     */
    private String automaticemail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getJavarule() {
        return javarule;
    }

    public void setJavarule(String javarule) {
        this.javarule = javarule;
    }
    public String getDefectrule() {
        return defectrule;
    }

    public void setDefectrule(String defectrule) {
        this.defectrule = defectrule;
    }
    public String getApproximatecode() {
        return approximatecode;
    }

    public void setApproximatecode(String approximatecode) {
        this.approximatecode = approximatecode;
    }
    public String getSqlrule() {
        return sqlrule;
    }

    public void setSqlrule(String sqlrule) {
        this.sqlrule = sqlrule;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getAutomaticscan() {
        return automaticscan;
    }

    public void setAutomaticscan(String automaticscan) {
        this.automaticscan = automaticscan;
    }
    public String getAutomaticemail() {
        return automaticemail;
    }

    public void setAutomaticemail(String automaticemail) {
        this.automaticemail = automaticemail;
    }

//    public TaskScanningScheme(Integer id, String javarule, String defectrule, String approximatecode, String sqlrule, String email, String automaticscan, String automaticemail) {
//        this.id = id;
//        this.javarule = javarule;
//        this.defectrule = defectrule;
//        this.approximatecode = approximatecode;
//        this.sqlrule = sqlrule;
//        this.email = email;
//        this.automaticscan = automaticscan;
//        this.automaticemail = automaticemail;
//    }

    @Override
    public String toString() {
        return "TaskScanningScheme{" +
            "id=" + id +
            ", javarule=" + javarule +
            ", defectrule=" + defectrule +
            ", approximatecode=" + approximatecode +
            ", sqlrule=" + sqlrule +
            ", email=" + email +
            ", automaticscan=" + automaticscan +
            ", automaticemail=" + automaticemail +
        "}";
    }


    /**
     * 默认构造方法,添加此默认构造方法即可解决问题
     * 创建一个新的实例
     *
     */
    public TaskScanningScheme(){
        super();
    }

    public TaskScanningScheme(String javarule) {
        this.javarule = javarule;
        this.automaticscan = "false";
        this.automaticemail = "false";
    }

    public TaskScanningScheme(Integer id, String javarule) {
        this.id = id;
        this.javarule = javarule;
        this.automaticscan = "false";
        this.automaticemail = "false";
    }
}
