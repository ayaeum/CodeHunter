package org.jeecg.modules.system.service;

import com.alibaba.fastjson.JSONArray;
import org.jeecg.modules.system.entity.TaskDatasTable;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 菜瓜皮
 * @since 2022-09-18
 */
@Service
public interface ITaskDatasTableService extends IService<TaskDatasTable> {

        /**
         * 插入任务数据
         * @param jeecg_account 系统用户名
         * @param taskName 任务名
         * @param taskDatasTable 任务数据
         * @return
         */
        public List<TaskDatasTable> insertTaskDatasTableService(String jeecg_account, String taskName,TaskDatasTable taskDatasTable);

        /**
         * 查询任务数据
         * @param jeecg_account
         * @param taskName
         * @return 任务数据列表
         */
        public List<TaskDatasTable> queryTaskDatasTable(String jeecg_account, String taskName);

        /**
         * 查询任务数据
         * @param jeecg_account
         * @param taskName
         * @return JSONArray格式的任务数据
         */
        public JSONArray queryTaskDatasTable_JSONArray(String jeecg_account, String taskName);
}
