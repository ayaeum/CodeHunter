package org.jeecg.modules.system.service;

import org.jeecg.modules.system.entity.TaskScanResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 菜瓜皮
 * @since 2023-02-25
 */
public interface ITaskScanResultService extends IService<TaskScanResult> {

    /**
     * 插入扫描结果
     * @param taskId
     * @param taskScanResult
     * @return
     */
    public boolean insertResult(int taskId,TaskScanResult taskScanResult);

    /**
     * 根据ID查询所有扫描结果
     * @param taskId
     * @return
     */
    public List<TaskScanResult> queryAllTaskResultsById(int taskId);
}
