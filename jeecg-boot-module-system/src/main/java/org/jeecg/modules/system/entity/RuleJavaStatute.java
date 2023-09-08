package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 菜瓜皮
 * @since 2023-02-16
 */
@TableName("rule_java_statute")
public class RuleJavaStatute implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规约编号
     */
    private Integer id;

    /**
     * 规约名
     */
    private String rule;

    /**
     * 规约内容
     */
    private String context;

    /**
     * 备注
     */
    private String remark;

    /**
     * 规约正例
     */
    private String example;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    @Override
    public String toString() {
        return "RuleJavaStatute{" +
            "id=" + id +
            ", rule=" + rule +
            ", context=" + context +
            ", remark=" + remark +
            ", example=" + example +
        "}";
    }
}
