package org.jeecg.modules.system.service;

import org.jeecg.modules.system.entity.TaskPathTable;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 菜瓜皮
 * @since 2022-09-22
 */
@Service
public interface ITaskPathTableService extends IService<TaskPathTable> {

    /**
     * 新建任务时初始化任务的文件列表
     * @param jeecg_account
     * @param taskName
     * @return
     */
    public int Init(String jeecg_account,String taskName);

    /**
     * 携带系统用户名和任务名查询文件表
     * @param jeecg_account
     * @param taskName
     * @return
     */
    public TaskPathTable queryTaskPathBySysnameandTaskName(String jeecg_account,String taskName);

    /**
     * 更新文件表
     * @param taskPathTable
     * @return
     */
    public TaskPathTable updateTaskPath(TaskPathTable taskPathTable);
}
