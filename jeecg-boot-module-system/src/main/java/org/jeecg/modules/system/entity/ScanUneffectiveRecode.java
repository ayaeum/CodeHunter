package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 菜瓜皮
 * @since 2023-04-26
 */
@TableName("scan_uneffective_recode")
public class ScanUneffectiveRecode implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer count;

    private Long problemid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getProblemid() {
        return problemid;
    }

    public void setProblemid(Long problemid) {
        this.problemid = problemid;
    }

    @Override
    public String toString() {
        return "ScanUneffectiveRecode{" +
                "id=" + id +
                ", count=" + count +
                ", problemid=" + problemid +
                '}';
    }
}
