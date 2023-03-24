package org.jeecg.modules.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.checkerframework.checker.units.qual.A;
import org.jeecg.modules.system.entity.TaskDetailsTable;
import org.jeecg.modules.system.mapper.TaskDetailsTableMapper;
import org.jeecg.modules.system.service.ITaskDetailsTableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 菜瓜皮
 * @since 2022-09-19
 */
@Service
public class TaskDetailsTableServiceImpl extends ServiceImpl<TaskDetailsTableMapper, TaskDetailsTable> implements ITaskDetailsTableService {

    @Autowired
    private TaskDetailsTableMapper taskDetailsTableMapper;


    /**
     * 携带系统用户名和任务名称获取用户所有问题详情
     * @param SysUserName
     * @param taskName
     * @return JSONArray
     */
    public JSONArray queryAllTaskDetailsBySysUserNameAndTaskName(String SysUserName, String taskName){
        QueryWrapper<TaskDetailsTable> qw = new QueryWrapper<TaskDetailsTable>();
        qw.eq("sysUsername",SysUserName).eq("taskName",taskName);
        JSONArray res = new JSONArray();
        res.addAll(taskDetailsTableMapper.selectList(qw));
        return res;
    }
}
