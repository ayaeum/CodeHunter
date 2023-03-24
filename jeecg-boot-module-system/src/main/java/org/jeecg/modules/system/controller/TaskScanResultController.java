package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import detector.DetectionRun;
import org.checkerframework.checker.units.qual.A;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.controller.Test.Enum;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.service.ITaskScanResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 菜瓜皮
 * @since 2023-02-25
 */
@RestController
@RequestMapping("/taskScanResult")
public class TaskScanResultController {

    @Autowired
    private ITaskScanResultService iTaskScanResultService;

    @GetMapping(value = "/queryAllTaskResults")
    public Result<List<TaskScanResult>> queryAllTaskResults(int id){
        List<TaskScanResult> taskScanResults = iTaskScanResultService.queryAllTaskResultsById(id);
        if (Objects.isNull(taskScanResults)) {
            return Result.error("查询失败或数据库出错，请重试");
        }
        return Result.OK(taskScanResults);
    }

    @GetMapping(value = "/queryCurrentTaskResult")
    public Result<TaskScanResult> queryCurrentTaskResult(int id,int order){
        List<TaskScanResult> taskScanResults = iTaskScanResultService.queryAllTaskResultsById(id);
        if (Objects.isNull(taskScanResults)) {
            return Result.error("查询失败或数据库出错，请重试");
        }
        return Result.OK(taskScanResults.get(taskScanResults.size()-order));
    }
}
