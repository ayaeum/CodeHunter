package org.jeecg.modules.system.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.entity.TaskPathTable;
import org.jeecg.modules.system.service.ITaskPathTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 菜瓜皮
 * @since 2022-09-22
 */
@Slf4j
@RestController
@RequestMapping("/taskPathTable")
public class TaskPathTableController {
    @Autowired
    private ITaskPathTableService iTaskPathTableService;

    @GetMapping(value = "/updateList")
    public TaskPathTable updateList(TaskPathTable taskPathTable){
        //存入数据库
        return iTaskPathTableService.updateTaskPath(taskPathTable);
    }

    @GetMapping(value = "/getList")
    public Result<TaskPathTable> getList(String sysName,String taskName){
        return Result.OK(iTaskPathTableService.queryTaskPathBySysnameandTaskName(sysName,taskName));
    }
}
