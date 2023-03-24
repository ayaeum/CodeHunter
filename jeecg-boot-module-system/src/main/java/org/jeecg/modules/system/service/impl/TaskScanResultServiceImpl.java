package org.jeecg.modules.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.system.entity.TaskScanResult;
import org.jeecg.modules.system.mapper.TaskScanResultMapper;
import org.jeecg.modules.system.service.ITaskScanResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 菜瓜皮
 * @since 2023-02-25
 */
@Service
public class TaskScanResultServiceImpl extends ServiceImpl<TaskScanResultMapper, TaskScanResult> implements ITaskScanResultService {
    @Autowired
    private TaskScanResultMapper taskScanResultMapper;

    /**
     * 插入扫描结果
     * @param taskScanResult
     * @return
     */
    public boolean insertResult(int taskId,TaskScanResult taskScanResult){
        //传入一个对象,根据taskID查询扫描结果表所有相关记录
        QueryWrapper<TaskScanResult> qw=new QueryWrapper<TaskScanResult>();
        qw.eq("id",taskId);
        List<TaskScanResult> taskScanResults = taskScanResultMapper.selectList(qw);
        //判断记录个数是否超过12条
        if(taskScanResults.size()<12){
            //记录不超过12,直接插入
            return taskScanResultMapper.insert(taskScanResult)==1;
        }
        //如果超过12条,则根据时间排序,删除事件最早的记录，插入新纪录
        TaskScanResult taskScanResult1=taskScanResults.get(0);
        for(int i=1;i<taskScanResults.size();i++){
            if(taskScanResults.get(i).getTimestamp().toInstant(ZoneOffset.ofHours(8)).toEpochMilli()<taskScanResult1.getTimestamp().toInstant(ZoneOffset.ofHours(8)).toEpochMilli()){
                taskScanResult1=taskScanResults.get(i);
            }
        }
        qw.eq("timestamp",taskScanResult1.getTimestamp());
        return taskScanResultMapper.delete(qw)==1&&taskScanResultMapper.insert(taskScanResult)==1;
    }

    /**
     * 根据ID查询所有扫描结果
     * @param taskId
     * @return
     */
    public List<TaskScanResult> queryAllTaskResultsById(int taskId){
        QueryWrapper<TaskScanResult> qw=new QueryWrapper<TaskScanResult>();
        qw.eq("id",taskId);
        List<TaskScanResult> taskScanResults = taskScanResultMapper.selectList(qw);
        //进行格式转换
//        for (TaskScanResult taskScanResult : taskScanResults) {
//            taskScanResult.setJavaruleresult();
//        }
        return taskScanResults;
    }
}
