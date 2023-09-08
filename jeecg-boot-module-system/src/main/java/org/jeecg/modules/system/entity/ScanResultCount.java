package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 菜瓜皮
 * @since 2023-05-15
 */
@TableName("scan_result_count")
public class ScanResultCount implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long count;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ScanResultCount{" +
            "id=" + id +
            ", count=" + count +
        "}";
    }
}
