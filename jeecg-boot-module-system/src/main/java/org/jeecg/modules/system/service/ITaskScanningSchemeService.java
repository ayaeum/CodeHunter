package org.jeecg.modules.system.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.system.entity.TaskScanningScheme;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 菜瓜皮
 * @since 2023-02-15
 */
public interface ITaskScanningSchemeService extends IService<TaskScanningScheme> {
    /**
     * 初始化扫描方案
     * @return
     */
    public boolean Init(int id);

    public TaskScanningScheme QueryTaskScanBySysnameAndTaskname(String Sysname,String Taskname);

    public TaskScanningScheme QueryTaskScanById(int id);

    /**
     * 根据任务ID更新Java规约配置
     * @param taskId 任务ID
     * @param protocolConfig 配置信息
     * @return
     */
    public boolean updateTaskScanById(int taskId,JSONObject protocolConfig);

    /**
     * 根据任务ID更新邮箱账号
     * @param taskId 任务ID
     * @param emailList 邮箱列表
     * @return
     */
    public boolean updateEmailById(int taskId,String emailList);

    /**
     * 根据任务ID更新邮件通知开关
     * @param taskId 任务ID
     * @param autoEmail 邮件通知开关
     * @return
     */
    public boolean updateAutoEmailById(int taskId,boolean autoEmail);

    /**
     * 根据任务ID更新邮件通知开关
     * @param taskId 任务id
     * @param sqlrule java规约检测开关
     * @param approximatecode 克隆代码检测开关
     * @param defectrule 缺陷检测开关
     * @return 布尔值
     */
    public boolean updateDetectionSwitchById(int taskId,boolean sqlrule,boolean approximatecode,boolean defectrule);
}
