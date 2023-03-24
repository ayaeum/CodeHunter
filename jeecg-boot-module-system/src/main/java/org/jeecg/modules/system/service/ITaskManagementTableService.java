package org.jeecg.modules.system.service;

import org.jeecg.modules.system.entity.CertificationManagementForm;
import org.jeecg.modules.system.entity.TaskManagementTable;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 菜瓜皮
 * @since 2022-08-29
 */
@Service
public interface ITaskManagementTableService extends IService<TaskManagementTable> {

    /**
     * 直接插入任务
     * @param taskManagementTable
     * @return
     */
    public boolean DirectInsertTask(TaskManagementTable taskManagementTable);

    /**
     * 返回系统用户所有任务
     * @param jeecg_account
     * @return
     */
    public List<TaskManagementTable> queryTaskBySysUserName(String jeecg_account);


    /**
     * 根据id查询任务信息
     * @param id
     * @return
     */
    public TaskManagementTable queryTaskById(int id);

    /**
     * 查询任务id
     * @param SysName
     * @param TaskName
     * @return
     */
//    public int queryTaskId(String SysName,String TaskName);

    /**
     * 携带系统用户名检查任务名是否占用
     * @param jeecg_account 系统用户名
     * @param taskName 标识名
     * @return boolean
     */
    public boolean queryTaskIsExistBySysnameandTaskName(String jeecg_account,String taskName);

    /**
     * 携带系统用户名和任务名查询任务主体
     * @param jeecg_account 系统用户名
     * @param taskName 任务名
     * @return 任务主体名称
     */
    public String QueryTaskOwner(String jeecg_account,String taskName);

    /**
     * 携带系统用户名和任务名称删除任务
     * @param jeecg_account 系统用户名
     * @param taskName
     * @return boolean
     */
    public boolean DeleteTask(String jeecg_account,String taskName);
}
