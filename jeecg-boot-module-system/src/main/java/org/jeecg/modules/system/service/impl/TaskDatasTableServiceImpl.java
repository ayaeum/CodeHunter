package org.jeecg.modules.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.system.entity.TaskDatasTable;
import org.jeecg.modules.system.mapper.TaskDatasTableMapper;
import org.jeecg.modules.system.mapper.TaskManagementTableMapper;
import org.jeecg.modules.system.service.ITaskDatasTableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 菜瓜皮
 * @since 2022-09-18
 */
@Service
public class TaskDatasTableServiceImpl extends ServiceImpl<TaskDatasTableMapper, TaskDatasTable> implements ITaskDatasTableService {

    @Autowired
    private TaskDatasTableMapper taskDatasTableMapper;

    /**
     * 插入任务数据
     * @param jeecg_account 系统用户名
     * @param taskName 任务名
     * @param taskDatasTable 任务数据
     * @return
     */
    public List<TaskDatasTable> insertTaskDatasTableService(String jeecg_account, String taskName, TaskDatasTable taskDatasTable){
        QueryWrapper<TaskDatasTable> qw = new QueryWrapper<TaskDatasTable>();
        qw.eq("sysUsername",jeecg_account).eq("taskName",taskName);
        int num = taskDatasTableMapper.selectList(qw).size();
        qw.clear();
        if(num==12) {
            qw.eq("sysUsername",jeecg_account).eq("taskName",taskName).eq("clone_rank",1);
            taskDatasTableMapper.delete(qw);
            qw.clear();

            qw.eq("sysUsername",jeecg_account).eq("taskName",taskName);
            List<TaskDatasTable> taskDatasTableList = taskDatasTableMapper.selectList(qw);
            qw.clear();

            Iterator<TaskDatasTable> iter = taskDatasTableList.iterator();
            while(iter.hasNext()){
                TaskDatasTable itercarry = iter.next();
                qw.eq("sysUsername",jeecg_account).eq("taskName",taskName).eq("clone_rank",itercarry.getCloneRank());
                itercarry.setCloneRank(itercarry.getCloneRank()-1);
                taskDatasTableMapper.update(itercarry,qw);
                qw.clear();
            }

            taskDatasTable.setCloneRank(12);
            taskDatasTableMapper.insert(taskDatasTable);
        }else{
            taskDatasTable.setCloneRank(num+1);
            taskDatasTableMapper.insert(taskDatasTable);
        }
        qw.eq("sysUsername",jeecg_account).eq("taskName",taskName);
        return taskDatasTableMapper.selectList(qw);
    }

    /**
     * 查询任务数据
     * @param jeecg_account
     * @param taskName
     * @return
     */
    public List<TaskDatasTable> queryTaskDatasTable(String jeecg_account, String taskName){
        QueryWrapper<TaskDatasTable> qw = new QueryWrapper<TaskDatasTable>();
        qw.eq("sysUsername",jeecg_account).eq("taskName",taskName);
        return taskDatasTableMapper.selectList(qw);
    }

    /**
     * 查询任务数据
     * @param jeecg_account
     * @param taskName
     * @return JSONArray格式的任务数据
     */
    public JSONArray queryTaskDatasTable_JSONArray(String jeecg_account, String taskName){
        QueryWrapper<TaskDatasTable> qw = new QueryWrapper<TaskDatasTable>();
        qw.eq("sysUsername",jeecg_account).eq("taskName",taskName);
        JSONArray res = new JSONArray();
        res.addAll(taskDatasTableMapper.selectList(qw));
        return res;
    }
}
