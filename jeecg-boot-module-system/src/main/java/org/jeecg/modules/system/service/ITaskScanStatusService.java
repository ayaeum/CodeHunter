package org.jeecg.modules.system.service;

import org.jeecg.modules.system.entity.TaskScanStatus;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 菜瓜皮
 * @since 2023-02-18
 */
public interface ITaskScanStatusService extends IService<TaskScanStatus> {
    public boolean Init(int id);
}
