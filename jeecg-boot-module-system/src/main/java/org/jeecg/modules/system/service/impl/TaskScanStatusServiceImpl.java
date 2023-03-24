package org.jeecg.modules.system.service.impl;

import org.jeecg.modules.system.entity.TaskScanStatus;
import org.jeecg.modules.system.mapper.TaskScanStatusMapper;
import org.jeecg.modules.system.service.ITaskScanStatusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 菜瓜皮
 * @since 2023-02-18
 */
@Service
public class TaskScanStatusServiceImpl extends ServiceImpl<TaskScanStatusMapper, TaskScanStatus> implements ITaskScanStatusService {

    @Resource
    private TaskScanStatusMapper taskScanStatusMapper;

    public boolean Init(int id){
        TaskScanStatus taskScanStatus=new TaskScanStatus(id);
        return taskScanStatusMapper.insert(taskScanStatus)==1;
    }
}
