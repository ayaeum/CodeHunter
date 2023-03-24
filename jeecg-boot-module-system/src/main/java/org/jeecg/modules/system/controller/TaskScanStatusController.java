package org.jeecg.modules.system.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import detector.RuleChecker;
import org.apache.tomcat.jni.Time;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.model.HttpClientUtil;
import org.jeecg.modules.system.service.*;
import org.jeecg.modules.system.util.MethodExtractorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Iterator;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 菜瓜皮
 * @since 2023-02-18
 */
@RestController
@RequestMapping("/taskScanStatus")
public class TaskScanStatusController {

    @Autowired
    private ITaskPathTableService iTaskPathTableService;

    @Autowired
    private ITaskManagementTableService iTaskManagementTableService;

    @Autowired
    private ICertificationManagementFormService iCertificationManagementFormService;

    @Autowired
    private IGitOptService iGitOptService;

    @Autowired
    private ITaskScanningSchemeService iTaskScanningSchemeService;

    @Autowired
    private ITaskScanResultService iTaskScanResultService;

    @Autowired
    private TemplateEngine templateEngine;

    @Resource
    private org.jeecg.modules.system.util.sendHtmlMail sendHtmlMail;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final ObjectMapper mapper = new ObjectMapper();

    @GetMapping(value = "/StartScan")
    public Result<JSONArray> StartScan(int id){
        //根据id查询任务信息
        TaskManagementTable taskManagementTable=iTaskManagementTableService.queryTaskById(id);
        //查询主体
        CertificationManagementForm certificationManagementForm=iCertificationManagementFormService.selectOneCer(taskManagementTable.getSysUsername(),taskManagementTable.getowner());
        //查询文件列表，拉取文件内容
        TaskPathTable taskPathTable=iTaskPathTableService.queryTaskPathBySysnameandTaskName(taskManagementTable.getSysUsername(),taskManagementTable.getTaskName());
        String[] filelist=taskPathTable.getOncheckFile().split(",");

        //拉取配置表
        TaskScanningScheme taskScanningScheme=iTaskScanningSchemeService.QueryTaskScanById(id);
        JSONObject config=JSONObject.parseObject(taskScanningScheme.getJavarule());

        //构造初始参数(平台、文件列表集合)
        JSONArray jsonArray=iGitOptService.getScanObj(filelist,certificationManagementForm,taskManagementTable);
        JSONArray jsonArray3=new JSONArray();//相似度检测需要的方法列表
        JSONArray defectResult = new JSONArray();
        TaskScanStatus taskScanStatus=new TaskScanStatus(id);

        try {//读写一次redis
            String s = mapper.writeValueAsString(taskScanStatus);
            stringRedisTemplate.opsForValue().set("scanning:"+id,s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        for(int i=0;i<jsonArray.size();i++){//JAVA规约扫描
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                CompilationUnit cu = StaticJavaParser.parse(jsonArray.getJSONObject(i).getString("code"));//生成相似度检测需要的方法列表
                new MethodExtractorUtil.MethodVisitor().visit(cu, null);
                jsonArray3.addAll(new MethodExtractorUtil.MethodVisitor().methodCutter(cu, jsonArray.getJSONObject(i).getString("filename")));//分割方法
            } catch (IOException e) {
                e.printStackTrace();
            }

            taskScanStatus.setScanmsg("正在扫描"+jsonArray.getJSONObject(i).getString("filename")+"....\n");
            try {
                String s = mapper.writeValueAsString(taskScanStatus);
                stringRedisTemplate.opsForValue().set("scanning:"+id,s);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            //扫描代码
//            String postHttpByPath = HttpClientUtil.createPostHttpByPath("http://172.20.2.152:5000/defectPrediction/single", jsonArray.getJSONObject(i).toJSONString());//发送请求进行缺陷扫描
//
//            if(!postHttpByPath.contains("500 Internal Server Error")){
//                JSONObject jsonObject2 = JSONObject.parseObject(postHttpByPath);
//                JSONObject jsonObject1 = new JSONObject();
//                jsonObject1.put("filename",jsonArray.getJSONObject(i).getString("filename"));
//                jsonObject1.put("result",JSONArray.parseArray(jsonObject2.getString("res")).get(0));
//                defectResult.add(jsonObject1);
//            }

            JSONObject jsonObject = RuleChecker.runByArrary(jsonArray.getJSONObject(i).getString("code"), config);
            jsonArray.getJSONObject(i).put("result", jsonObject);
            taskScanStatus.setStatus(taskScanStatus.getStatus()+jsonArray.getJSONObject(i).getString("filename")+"扫描完成。\n");
            taskScanStatus.setProcess(String.valueOf((i+1)*100/jsonArray.size()));
            try {
                String s = mapper.writeValueAsString(taskScanStatus);
                stringRedisTemplate.opsForValue().set("scanning:"+id,s);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        taskScanStatus.setScanmsg("正在进行克隆问题扫描...\n");
        taskScanStatus.setProcess1("0");
        try {
            String s = mapper.writeValueAsString(taskScanStatus);
            stringRedisTemplate.opsForValue().set("scanning:"+id,s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        TaskScanResult taskScanResult=new TaskScanResult(id, LocalDateTime.now(),jsonArray.toJSONString());
        JSONArray cloneResult = new JSONArray();
        JSONArray jsonArray4=new JSONArray();
        for(int k=1;k<=jsonArray3.size();k++){
            jsonArray4.add(jsonArray3.get(k-1));
            if(k%18==0||k==jsonArray3.size()){//把每8个方法为单位传到python web进行检测
                String postHttpByPath = HttpClientUtil.createPostHttpByPath("http://172.20.2.152:5000/cloneDetection/", jsonArray4.toJSONString());
                if(!postHttpByPath.equals("error")){//如果检测出错误，那么不加入结果
                    cloneResult.addAll(JSONObject.parseObject(postHttpByPath).getJSONArray("res"));
                }
                System.out.println(jsonArray4);
                jsonArray4.clear();//清空数组，避免重复

                taskScanStatus.setProcess1(String.valueOf(k*100/jsonArray3.size()));//写进度到redis
                try {
                    String s = mapper.writeValueAsString(taskScanStatus);
                    stringRedisTemplate.opsForValue().set("scanning:"+id,s);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }

        taskScanStatus.setScanmsg("任务结束。\n");
        taskScanStatus.setStatus(taskScanStatus.getStatus()+"扫描任务完成，请点击右上角关闭此窗口，详情请点击问题列表查看。\n");
        taskScanStatus.setProcess("100");
        try {
            String s = mapper.writeValueAsString(taskScanStatus);
            stringRedisTemplate.opsForValue().set("scanning:"+id,s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        taskScanResult.setCloneresult(cloneResult.toJSONString());//漏洞检测扫描结果存入扫描结果
        taskScanResult.setDefectresult(defectResult.toJSONString());//缺陷扫描结果存入扫描结果
        cloneResult.clear();
        defectResult.clear();
        if (iTaskScanResultService.insertResult(id,taskScanResult)) {//存入数据库
            JSONArray jsonArray1 = JSONArray.parseArray(taskScanResult.getJavaruleresult());
            //统计任务信息
//            int serious=0,warn=0,propose=0;
//            for(int k=0;k<jsonArray1.size();k++){
//                JSONObject jsonObject = jsonArray1.getJSONObject(k);
//                JSONArray jsonArray2 = JSONArray.parseArray(jsonObject.getString("result"));
//                for(int j=0;j<jsonArray2.size();j++){
//                    switch (jsonArray2.getJSONObject(j).getString("description").substring(1,3)){
//                        case "严重":
//                            serious++;
//                            break;
//                        case "警告":
//                            warn++;
//                            break;
//                        case "建议":
//                            propose++;
//                            break;
//                    }
//                }
//            }

            //邮件通知
//            if(taskScanningScheme.getAutomaticemail().equals("true")){
//                //构造邮件内容
//                Context ctx = new Context();
//                ctx.setVariable("timestamp", taskScanResult.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//                ctx.setVariable("task", taskManagementTable.getTaskName());
//                ctx.setVariable("repository", taskManagementTable.getWareHouse());
//                ctx.setVariable("branch", taskManagementTable.getBranch());
//                ctx.setVariable("total", serious+warn+propose);
//                ctx.setVariable("serious", serious);
//                ctx.setVariable("warn", warn);
//                ctx.setVariable("propose", propose);
//                String mail = templateEngine.process("scanresult.html", ctx);
//                String[] emailList=taskScanningScheme.getEmail().substring(1,taskScanningScheme.getEmail().length()-1).split(",");
//                for (String s : emailList) {
//                    sendHtmlMail.sendHtmlMails("1192129669@qq.com", s, "扫描结果通知", mail);
//                }
//            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stringRedisTemplate.delete("scanning:"+id);
            return Result.ok(jsonArray);
        }

        return Result.error("扫描失败或数据库出错，请重试");
    }

    @GetMapping(value = "/queryScanStatus")
    public Result<TaskScanStatus> queryScanStatus(int id){
        TaskScanStatus taskScanStatus=null;
        try {
            String s = stringRedisTemplate.opsForValue().get("scanning:" + id);
            if(s!=null){
                taskScanStatus=mapper.readValue(s,TaskScanStatus.class);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Result.ok(taskScanStatus);
    }
}
