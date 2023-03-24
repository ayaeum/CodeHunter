package org.jeecg.modules.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.system.entity.TaskManagementTable;
import org.jeecg.modules.system.entity.TaskScanningScheme;
import org.jeecg.modules.system.mapper.TaskDetailsTableMapper;
import org.jeecg.modules.system.mapper.TaskManagementTableMapper;
import org.jeecg.modules.system.mapper.TaskScanningSchemeMapper;
import org.jeecg.modules.system.service.ITaskManagementTableService;
import org.jeecg.modules.system.service.ITaskScanningSchemeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 菜瓜皮
 * @since 2023-02-15
 */
@Service
public class TaskScanningSchemeServiceImpl extends ServiceImpl<TaskScanningSchemeMapper, TaskScanningScheme> implements ITaskScanningSchemeService {

    @Resource
    private TaskScanningSchemeMapper taskScanningSchemeMapper;

    @Resource
    private TaskManagementTableMapper taskManagementTableMapper;

    static final JSONObject jsonObject=new JSONObject();
    static {
        jsonObject.put("1","严重");
        jsonObject.put("2","警告");
        jsonObject.put("3","建议");
    }


    /**
     * 初始化扫描方案
     * @return
     */
    public boolean Init(int id){
        TaskScanningScheme taskScanningScheme=new TaskScanningScheme(id,jsonObject.toJSONString(),"false","false","true",null,"false","false");
        return taskScanningSchemeMapper.insert(taskScanningScheme)==1;
    }

    public TaskScanningScheme QueryTaskScanBySysnameAndTaskname(String Sysname,String Taskname){
        //首先要查出任务id
        int res=-1;
        QueryWrapper<TaskManagementTable> qw = new QueryWrapper<TaskManagementTable>();
        qw.eq("sysUsername",Sysname).eq("taskName",Taskname);
        if(taskManagementTableMapper.selectOne(qw)!=null){
            res= taskManagementTableMapper.selectOne(qw).getId();
        }
        //根据任务数据id查出配置信息返回
        if(res!=-1){
            QueryWrapper<TaskScanningScheme> qw1 = new QueryWrapper<TaskScanningScheme>();
            qw1.eq("id",res);
            taskScanningSchemeMapper.selectOne(qw1);
            return taskScanningSchemeMapper.selectOne(qw1);
        }else{
            return null;
        }
    }

    public TaskScanningScheme QueryTaskScanById(int id){
        QueryWrapper<TaskScanningScheme> qw = new QueryWrapper<TaskScanningScheme>();
        qw.eq("id",id);
        return taskScanningSchemeMapper.selectOne(qw);
    }

    /**
     * 根据任务ID更新Java规约配置
     * @param taskId 任务ID
     * @param protocolConfig 配置信息
     * @return 更新结果
     */
    public boolean updateTaskScanById(int taskId,JSONObject protocolConfig){
        QueryWrapper<TaskScanningScheme> qw = new QueryWrapper<TaskScanningScheme>();
        qw.eq("id",taskId);
        TaskScanningScheme taskScanningScheme = taskScanningSchemeMapper.selectOne(qw);
        taskScanningScheme.setJavarule(String.valueOf(protocolConfig));
        return taskScanningSchemeMapper.updateById(taskScanningScheme)==1;
    }

    /**
     * 根据任务ID更新邮箱账号
     * @param taskId 任务ID
     * @param emailList 邮箱列表
     * @return
     */
    public boolean updateEmailById(int taskId,String emailList){
        QueryWrapper<TaskScanningScheme> qw = new QueryWrapper<TaskScanningScheme>();
        qw.eq("id",taskId);
        TaskScanningScheme taskScanningScheme = taskScanningSchemeMapper.selectOne(qw);
        taskScanningScheme.setEmail(String.valueOf(emailList));
        return taskScanningSchemeMapper.updateById(taskScanningScheme)==1;
    }

    /**
     * 根据任务ID更新邮件通知开关
     * @param taskId 任务ID
     * @param autoEmail 邮件通知开关
     * @return
     */
    public boolean updateAutoEmailById(int taskId,boolean autoEmail){
        QueryWrapper<TaskScanningScheme> qw = new QueryWrapper<TaskScanningScheme>();
        qw.eq("id",taskId);
        TaskScanningScheme taskScanningScheme = taskScanningSchemeMapper.selectOne(qw);
        taskScanningScheme.setAutomaticemail(String.valueOf(autoEmail));
        return taskScanningSchemeMapper.updateById(taskScanningScheme)==1;
    }

    /**
     * 根据任务ID更新邮件通知开关
     * @param taskId 任务id
     * @param sqlrule java规约检测开关
     * @param approximatecode 克隆代码检测开关
     * @param defectrule 缺陷检测开关
     * @return 布尔值
     */
    public boolean updateDetectionSwitchById(int taskId,boolean sqlrule,boolean approximatecode,boolean defectrule){
        QueryWrapper<TaskScanningScheme> qw = new QueryWrapper<TaskScanningScheme>();
        qw.eq("id",taskId);
        TaskScanningScheme taskScanningScheme = taskScanningSchemeMapper.selectOne(qw);
        taskScanningScheme.setSqlrule(String.valueOf(sqlrule));
        taskScanningScheme.setApproximatecode(String.valueOf(approximatecode));
        taskScanningScheme.setDefectrule(String.valueOf(defectrule));
        return taskScanningSchemeMapper.updateById(taskScanningScheme)==1;
    }
}
