package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.mapper.TaskManagementTableMapper;
import org.jeecg.modules.system.service.ITaskManagementTableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.system.service.ITaskPathTableService;
import org.jeecg.modules.system.service.ITaskScanningSchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 菜瓜皮
 * @since 2022-08-29
 */
@Service
public class TaskManagementTableServiceImpl extends ServiceImpl<TaskManagementTableMapper, TaskManagementTable> implements ITaskManagementTableService {


    @Resource
    private TaskManagementTableMapper taskManagementTableMapper;

    @Resource
    private ITaskScanningSchemeService iTaskScanningSchemeService;

    @Resource
    private ITaskPathTableService iTaskPathTableService;

    /**
     * 直接插入任务
     * @param taskManagementTable
     * @return
     */
    public boolean DirectInsertTask(TaskManagementTable taskManagementTable){
        return taskManagementTableMapper.insert(taskManagementTable)==1&&iTaskScanningSchemeService.Init(taskManagementTable.getId())&&iTaskPathTableService.Init(taskManagementTable.getSysUsername(),taskManagementTable.getTaskName())==1;
    }

    /**
     * 查询任务id,若任务不存在则返回-1
     * @param SysName
     * @param TaskName
     * @return
     */
//    public int queryTaskId(String SysName,String TaskName){
//        QueryWrapper<TaskManagementTable> qw = new QueryWrapper<TaskManagementTable>();
//        qw.eq("sysUsername",SysName).eq("taskName",TaskName);
//        if(taskManagementTableMapper.selectOne(qw)!=null){
//            return taskManagementTableMapper.selectOne(qw).getId();
//        }
//        return -1;
//    }

    /**
     * 根据id查询任务信息
     * @param id
     * @return
     */
    public TaskManagementTable queryTaskById(int id){
            QueryWrapper<TaskManagementTable> qw = new QueryWrapper<TaskManagementTable>();
            qw.eq("id",id);
            if(taskManagementTableMapper.selectOne(qw)!=null){
                return taskManagementTableMapper.selectOne(qw);
            }
            return null;
        }

    /**
     * 返回系统用户所有任务
     * @param jeecg_account 系统用户名
     * @return 任务列表
     */
    public List<TaskManagementTable> queryTaskBySysUserName(String jeecg_account){
        QueryWrapper<TaskManagementTable> qw = new QueryWrapper<TaskManagementTable>();
        qw.eq("sysUsername",jeecg_account);
        return taskManagementTableMapper.selectList(qw);
    }

    /**
     * 携带系统用户名检查任务名是否占用
     * @param jeecg_account 系统用户名
     * @param taskName 标识名
     * @return boolean
     */
    public boolean queryTaskIsExistBySysnameandTaskName(String jeecg_account,String taskName){
        QueryWrapper<TaskManagementTable> qw = new QueryWrapper<TaskManagementTable>();
        qw.eq("sysUsername",jeecg_account).eq("taskName",taskName);
        List<TaskManagementTable> queryresult = taskManagementTableMapper.selectList(qw);
        return queryresult.size() != 0;
    }

    /**
     * 携带系统用户名和任务名查询任务主体
     * @param jeecg_account 系统用户名
     * @param taskName 任务名
     * @return 任务主体名称
     */
    public String QueryTaskOwner(String jeecg_account,String taskName){
        QueryWrapper<TaskManagementTable> qw = new QueryWrapper<TaskManagementTable>();
        qw.eq("sysUsername",jeecg_account).eq("taskName",taskName);
        List<TaskManagementTable> queryresult = taskManagementTableMapper.selectList(qw);
        return queryresult.get(0).getowner();
    }

    /**
     * 携带系统用户名和任务名删除任务
     * @param jeecg_account 系统用户名
     * @param taskName 任务名
     * @return boolean
     */
    public boolean DeleteTask(String jeecg_account,String taskName){
        QueryWrapper<TaskManagementTable> qw = new QueryWrapper<TaskManagementTable>();
        qw.eq("sysUsername",jeecg_account).eq("taskName",taskName);
        TaskManagementTable taskManagementTable = taskManagementTableMapper.selectOne(qw);
        QueryWrapper<TaskPathTable> qw1=new QueryWrapper<>();
        qw1.eq("sysUsername",jeecg_account).eq("taskName",taskName);
        return iTaskPathTableService.remove(qw1)&&iTaskScanningSchemeService.removeById(taskManagementTable.getId())&&taskManagementTableMapper.delete(qw)==1;
    }
}
