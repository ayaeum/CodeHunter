package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.sf.json.JSONArray;
import org.checkerframework.checker.units.qual.C;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.controller.Test.Enum;
import org.jeecg.modules.system.entity.CertificationManagementForm;
import org.jeecg.modules.system.entity.TaskScanningScheme;
import org.jeecg.modules.system.service.ITaskScanningSchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 菜瓜皮
 * @since 2023-02-15
 */
@Slf4j
@RestController
@RequestMapping("/taskScanningScheme")
public class TaskScanningSchemeController {

    @Autowired
    private ITaskScanningSchemeService iTaskScanningSchemeService;

    @GetMapping(value = "/queryTaskScan")
    public TaskScanningScheme queryTaskScan(@RequestParam("Sysname")String Sysname, @RequestParam("Taskname")String Taskname){
        return iTaskScanningSchemeService.QueryTaskScanBySysnameAndTaskname(Sysname,Taskname);
    }

    @GetMapping(value = "/queryTaskScanById")
    public Result<TaskScanningScheme> queryTaskScanById(@RequestParam("taskId")int taskId){
        return Result.OK(iTaskScanningSchemeService.QueryTaskScanById(taskId));
    }

    @GetMapping(value = "/updateTaskScan")
    public Result<TaskScanningScheme> updateTaskScan(@RequestParam("taskId")String taskId, @RequestParam("configList")String configList){
        Result<TaskScanningScheme> resultobj = new Result<TaskScanningScheme>();
        //构造规则对象
        JSONArray jsonArray=JSONArray.fromObject(configList);
        JSONObject jsonObject=new JSONObject();
        for (int i=0;i<jsonArray.size();i++){
            jsonObject.put(jsonArray.getJSONObject(i).getString("id"),jsonArray.getJSONObject(i).getString("remark"));
        }
        resultobj.setCode(Enum.REQUEST_NORMAL);
        //将规则对象传入到服务层处理
        if(iTaskScanningSchemeService.updateTaskScanById(Integer.parseInt(taskId),jsonObject)){
            resultobj.setMessage(Enum.UPDATE_SUCCESS);
            resultobj.setResult(iTaskScanningSchemeService.QueryTaskScanById(Integer.parseInt(taskId)));
            return resultobj;
        }
        resultobj.setMessage(Enum.UPDATE_ERROR);
        resultobj.setResult(null);
        return resultobj;
    }

    @GetMapping(value = "/updateEmail")
    public Result<TaskScanningScheme> updateEmail(@RequestParam("taskId")String taskId, @RequestParam("emailList")String[] emailList){
        Result<TaskScanningScheme> resultobj = new Result<TaskScanningScheme>();
        //将邮箱列表传入到服务层处理
        if(iTaskScanningSchemeService.updateEmailById(Integer.parseInt(taskId), Arrays.toString(emailList))){
            resultobj.setMessage(Enum.UPDATE_SUCCESS);
            resultobj.setResult(iTaskScanningSchemeService.QueryTaskScanById(Integer.parseInt(taskId)));
            return resultobj;
        }
        resultobj.setMessage(Enum.UPDATE_ERROR);
        resultobj.setResult(null);
        return null;
    }

    @GetMapping(value = "/updateAutoEmail")
    public Result<TaskScanningScheme> updateEmail(@RequestParam("taskId")String taskId, @RequestParam("autoEmail")boolean autoEmail){
        Result<TaskScanningScheme> resultobj = new Result<TaskScanningScheme>();
        //将邮箱列表传入到服务层处理
        if(iTaskScanningSchemeService.updateAutoEmailById(Integer.parseInt(taskId), autoEmail)){
            resultobj.setMessage(Enum.UPDATE_SUCCESS);
            resultobj.setResult(iTaskScanningSchemeService.QueryTaskScanById(Integer.parseInt(taskId)));
            return resultobj;
        }
        resultobj.setMessage(Enum.UPDATE_ERROR);
        resultobj.setResult(null);
        return null;
    }

    @GetMapping(value = "/updateDetectionSwitchById")
    public Result<TaskScanningScheme> updateDetectionSwitchById(@RequestParam("taskId")String taskId, @RequestParam("sqlrule")boolean sqlrule,@RequestParam("approximatecode")boolean approximatecode,@RequestParam("defectrule")boolean defectrule){
        Result<TaskScanningScheme> resultobj = new Result<TaskScanningScheme>();
        if(iTaskScanningSchemeService.updateDetectionSwitchById(Integer.parseInt(taskId), sqlrule,approximatecode,defectrule)){
            resultobj.setMessage(Enum.UPDATE_SUCCESS);
            resultobj.setResult(iTaskScanningSchemeService.QueryTaskScanById(Integer.parseInt(taskId)));
            return resultobj;
        }
        resultobj.setMessage(Enum.UPDATE_ERROR);
        resultobj.setResult(null);
        return null;
    }
}
