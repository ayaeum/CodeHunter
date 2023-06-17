package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.controller.Test.Enum;
import org.jeecg.modules.system.entity.TaskScanningScheme;
import org.jeecg.modules.system.service.IScanResultCountService;
import org.jeecg.modules.system.service.IScanUneffectiveRecodeService;
import org.jeecg.modules.system.service.ITaskScanningSchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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

    @Autowired
    private IScanUneffectiveRecodeService iScanUneffectiveRecodeService;

    @Autowired
    private TemplateEngine templateEngine;  //使用TemplateEngine对象

    @Autowired
    private IScanResultCountService iScanResultCountService;

    public void buildPage() throws IOException {
        //数据
        Context context = new Context();
        context.setVariable("hello", "hello thymeleaf!");

        //文件输出的路径及文件名
        FileWriter writer = new FileWriter("E:\\temp\\page\\hello.html");

        templateEngine.process("hello", context, writer);  //参数：模板，数据，文件输出流
        //关闭文件
        writer.close();
    }

    /**
     * 网络钩子自动检测
     */
    @PostMapping(value = "/test")
    public Result<String> test(@RequestBody(required = false)JSONObject jsonObject){
        System.out.println(jsonObject.getString("ID"));
        List<List<String>> list=new LinkedList<List<String>>();
        for (int i = 0; i < 10; i++) {
            List<String> list1=new LinkedList<String>();
            list1.add("JeecgSystemApplication");
            list1.add("JeecgSystemApplication");
            list1.add("35");
            list1.add("长命名约定");
            list1.add("35 function sayHello(name) {\n" +
                    "36 console.log(\"Hello, \" + name + \"!\");\n" +
                    "37 }\n" +
                    "38 sayHello(\"John\");");
            list1.add("按钮");
            list.add(list1);
        }

        FileWriter writer = null;
        String filePath="C:\\AICodeHunter\\wensScanResult.html";
        if (iScanResultCountService.addScanResultCount(10)) {
            //文件输出的路径及文件名
            try {
                //数据
                Context context = new Context();
                context.setVariable("data1", list);
                writer = new FileWriter(filePath);
                templateEngine.process("wensScanResult", context, writer);  //参数：模板，数据，文件输出流
                //关闭文件
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new Result<String>(200,filePath);
        }
        return new Result<String>(500,null);
    }

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

    @PostMapping(value = "/updateTaskScanSchema")
    public Result<TaskScanningScheme> updateTaskScanSchema(@RequestBody JSONObject jsonObject1){
        System.out.println(jsonObject1);
        String taskId = jsonObject1.getString("taskId");
        String configList = jsonObject1.getString("configList");
        System.out.println(taskId);
        System.out.println(configList);
        Result<TaskScanningScheme> resultobj = new Result<TaskScanningScheme>();
        //构造规则对象
        JSONArray jsonArray=JSONArray.fromObject(configList);
        System.out.println(jsonArray);
        JSONObject jsonObject=new JSONObject();
        for (int i=0;i<jsonArray.size();i++){
            System.out.println(jsonArray.getJSONObject(i));
            System.out.println(jsonArray.getJSONObject(i).getString("remark"));
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
//        return null;
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
