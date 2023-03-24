package org.jeecg.modules.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.system.entity.TaskScanResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 菜瓜皮
 * @since 2023-02-25
 */
@Mapper
@Repository
public interface TaskScanResultMapper extends BaseMapper<TaskScanResult> {

}
