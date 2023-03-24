package org.jeecg.modules.system.service;

import com.alibaba.fastjson.JSONArray;
import org.jeecg.modules.system.entity.TaskDetailsTable;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 菜瓜皮
 * @since 2022-09-19
 */
@Service
public interface ITaskDetailsTableService extends IService<TaskDetailsTable> {

    /**
     * 携带系统用户名和任务名称获取用户所有问题详情
     * @param SysUserName
     * @param taskName
     * @return JSONArray
     */
    public JSONArray queryAllTaskDetailsBySysUserNameAndTaskName(String SysUserName,String taskName);
}
