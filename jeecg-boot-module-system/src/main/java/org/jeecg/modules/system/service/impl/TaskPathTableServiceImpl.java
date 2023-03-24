package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.system.entity.TaskPathTable;
import org.jeecg.modules.system.mapper.TaskPathTableMapper;
import org.jeecg.modules.system.service.ITaskPathTableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 菜瓜皮
 * @since 2022-09-22
 */
@Service
public class TaskPathTableServiceImpl extends ServiceImpl<TaskPathTableMapper, TaskPathTable> implements ITaskPathTableService {

    @Autowired
    private TaskPathTableMapper taskPathTableMapper;

    /**
     * 新建任务时初始化任务的文件列表
     * @param jeecg_account
     * @param taskName
     * @return
     */
    public int Init(String jeecg_account,String taskName){

        TaskPathTable taskPathTable=new TaskPathTable(jeecg_account,taskName);
        return taskPathTableMapper.insert(taskPathTable);
    }

    /**
     * 携带系统用户名和任务名查询文件表
     * @param jeecg_account
     * @param taskName
     * @return
     */
    public TaskPathTable queryTaskPathBySysnameandTaskName(String jeecg_account,String taskName){
        QueryWrapper<TaskPathTable> qw = new QueryWrapper<TaskPathTable>();
        qw.eq("sysUsername",jeecg_account).eq("taskName",taskName);
        return taskPathTableMapper.selectOne(qw);
    }

    /**
     * 更新文件表
     * @param taskPathTable
     * @return
     */
    public TaskPathTable updateTaskPath(TaskPathTable taskPathTable){
        QueryWrapper<TaskPathTable> qw = new QueryWrapper<TaskPathTable>();
        qw.eq("sysUsername",taskPathTable.getSysUsername()).eq("taskName",taskPathTable.getTaskName());
        if(taskPathTable.getOncheckFile()==null){
            taskPathTable.setOncheckFile("");
        }
        taskPathTableMapper.update(taskPathTable,qw);
        return taskPathTableMapper.selectOne(qw);
    }
}
