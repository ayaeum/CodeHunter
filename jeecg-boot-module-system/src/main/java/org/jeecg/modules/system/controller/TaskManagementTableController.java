package org.jeecg.modules.system.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.val;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.controller.Test.Enum;
import org.jeecg.modules.system.entity.TaskManagementTable;
import org.jeecg.modules.system.service.ITaskManagementTableService;
import org.jeecg.modules.system.service.impl.SysTenantServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 菜瓜皮
 * @since 2022-08-29
 */
@RestController
@RequestMapping("/taskManagementTable")
public class TaskManagementTableController {

    @Autowired
    private ITaskManagementTableService iTaskManagementTableService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 新增任务，先查询任务名存不存在，不存在则加之
     * @param taskManagementTable
     * @return
     */
    @GetMapping(value = "/insertTask")
    public Result<List<TaskManagementTable>> insertTask(TaskManagementTable taskManagementTable){
        Result<List<TaskManagementTable>> result = new Result<List<TaskManagementTable>>();
        if(!iTaskManagementTableService.queryTaskIsExistBySysnameandTaskName(taskManagementTable.getSysUsername(),taskManagementTable.getTaskName())){
            iTaskManagementTableService.DirectInsertTask(taskManagementTable);//插入任务信息到数据库
            result.setResult(iTaskManagementTableService.queryTaskBySysUserName(taskManagementTable.getSysUsername()));
        }
        else{
            result.setMessage(Enum.ADD_ERROR+Enum.TASKNAME_ALREADY_EXIST);
        }
        result.setCode(Enum.REQUEST_NORMAL);
        result.setResult(iTaskManagementTableService.queryTaskBySysUserName(taskManagementTable.getSysUsername()));
        return result;
    }

    /**
     * 携带系统用户名查找任务
     * @param jeecg_account 系统用户名
     * @return 任务列表
     */
    @GetMapping(value = "queryTaskBySysName")
    public Result<List<TaskManagementTable>> queryTaskBySysName(@RequestParam("jeecg_account")String jeecg_account){
        Result<List<TaskManagementTable>> resultobj = new Result<List<TaskManagementTable>>();
//        String json = stringRedisTemplate.opsForValue().get("tasklist:"+jeecg_account);
//        if(StrUtil.isNotBlank(json)){
//            resultobj.setCode(Enum.REQUEST_NORMAL);
//            resultobj.setMessage(Enum.QUERY_SUCCESS);
//            try {
//                CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, TaskManagementTable.class);
//                resultobj.setResult(objectMapper.readValue(json,listType));
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//            return resultobj;
//        }
        QueryWrapper<TaskManagementTable> qw = new QueryWrapper<TaskManagementTable>();
        qw.eq("sysUsername",jeecg_account);
        List<TaskManagementTable> result = iTaskManagementTableService.list(qw);
//        if(result == null){
//            stringRedisTemplate.opsForValue().set("tasklist:"+jeecg_account,"",10, TimeUnit.MINUTES);
//            resultobj.setCode(Enum.REQUEST_ERROR);
//            resultobj.setMessage(Enum.QUERY_NULL);
//            resultobj.setResult(null);
//            return resultobj;
//        }
//        try {
//            stringRedisTemplate.opsForValue().set("tasklist:"+jeecg_account,objectMapper.writeValueAsString(result),30, TimeUnit.MINUTES);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
        resultobj.setCode(Enum.REQUEST_NORMAL);
        resultobj.setMessage(Enum.QUERY_SUCCESS);
        resultobj.setResult(result);
        return resultobj;
    }


    /**
     * 携带系统用户名和任务名删除任务
     * @param jeecg_account 系统用户名
     * @param taskName 任务名
     * @return 最新的任务列表
     */
    @GetMapping(value = "/deleteTaskBySysNameandTaskName")
    public Result<List<TaskManagementTable>> deleteTaskBySysNameandTaskName(@RequestParam("jeecg_account")String jeecg_account,@RequestParam("taskName")String taskName){
        Result<List<TaskManagementTable>> resultobj = new Result<List<TaskManagementTable>>();
        QueryWrapper<TaskManagementTable> qw = new QueryWrapper<TaskManagementTable>();
        qw.eq("sysUsername",jeecg_account).eq("taskName",taskName);
        List<TaskManagementTable> tasklist = null;
        if(!iTaskManagementTableService.queryTaskIsExistBySysnameandTaskName(jeecg_account,taskName)){

            resultobj.setMessage(Enum.DELETE_FAILED+Enum.TASK_NOT_EXIST);
            tasklist = iTaskManagementTableService.queryTaskBySysUserName(jeecg_account);
        }
        else{
            resultobj.setMessage(Enum.DELETE_SUCCESS);
            iTaskManagementTableService.DeleteTask(jeecg_account,taskName);
            tasklist = iTaskManagementTableService.queryTaskBySysUserName(jeecg_account);
//            try {
//                stringRedisTemplate.opsForValue().set("tasklist:"+jeecg_account,objectMapper.writeValueAsString(tasklist),30, TimeUnit.MINUTES);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
        }
        resultobj.setCode(Enum.REQUEST_NORMAL);
        resultobj.setResult(tasklist);
        return resultobj;
    }
}
