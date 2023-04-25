package org.jeecg.modules.system.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import detector.RuleChecker;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.model.HttpClientUtil;
import org.jeecg.modules.system.service.*;
import org.jeecg.modules.system.util.CodeUtil;
import org.jeecg.modules.system.util.MethodExtractorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

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
    TemplateEngine templateEngine;

    @Resource
    private org.jeecg.modules.system.util.sendHtmlMail sendHtmlMail;

    @Autowired
    private ITaskScanResultService iTaskScanResultService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CodeUtil codeUtil;

    private static final ObjectMapper mapper = new ObjectMapper();

    @GetMapping(value = "/StartScan")
    public Result<JSONArray> StartScan(int id){
        TaskManagementTable taskManagementTable=iTaskManagementTableService.queryTaskById(id);//根据id查询任务信息
        CertificationManagementForm certificationManagementForm=iCertificationManagementFormService.selectOneCer(taskManagementTable.getSysUsername(),taskManagementTable.getowner());//查询主体
        TaskPathTable taskPathTable=iTaskPathTableService.queryTaskPathBySysnameandTaskName(taskManagementTable.getSysUsername(),taskManagementTable.getTaskName());//查询文件列表，拉取文件内容
        String[] filelist=taskPathTable.getOncheckFile().split(",");//文件列表
        //TODO 判断文件文件是否为空
        TaskScanningScheme taskScanningScheme=iTaskScanningSchemeService.QueryTaskScanById(id);//拉取配置表
        JSONObject config=JSONObject.parseObject(taskScanningScheme.getJavarule());//Java配置
        JSONArray jsonArray=iGitOptService.getScanObj(filelist,certificationManagementForm,taskManagementTable);//构造初始参数(平台、文件列表集合)
        JSONArray jsonArray3=new JSONArray();//相似度检测需要的方法列表
        JSONArray defectResult = new JSONArray();//缺陷扫描结果

        TaskScanStatus taskScanStatus=new TaskScanStatus(id);
        writeRedis(taskScanStatus,id);

        if (taskScanningScheme.getDefectrule().equals("false")) {
            SetTaskScanStatus(taskScanStatus,"本次不进行代码缺陷扫描。\n",taskScanStatus.getStatus()+"本次不进行代码缺陷扫描。\n",taskScanStatus.getProcess(),taskScanStatus.getProcess1());
        }
        if (taskScanningScheme.getSqlrule().equals("false")) {
            SetTaskScanStatus(taskScanStatus,"本次不进行JAVA规约扫描。\n",taskScanStatus.getStatus()+"本次不进行JAVA规约扫描。\n",taskScanStatus.getProcess(),taskScanStatus.getProcess1());
        }

        for(int i=0;i<jsonArray.size();i++){//JAVA规约扫描、代码缺陷检测

            try {
                CompilationUnit cu = StaticJavaParser.parse(jsonArray.getJSONObject(i).getString("code"));//生成相似度检测需要的方法列表
                new MethodExtractorUtil.MethodVisitor().visit(cu, null);
                jsonArray3.addAll(new MethodExtractorUtil.MethodVisitor().methodCutter(cu, jsonArray.getJSONObject(i).getString("filename")));//分割方法
            } catch (IOException e) {
                e.printStackTrace();
            }

            taskScanStatus.setScanmsg("正在扫描"+jsonArray.getJSONObject(i).getString("filename")+"....\n");
            writeRedis(taskScanStatus,id);
            if (taskScanningScheme.getDefectrule().equals("true")) {//代码缺陷扫描
                String postHttpByPath = HttpClientUtil.createPostHttpByPath("http://8.134.53.160:5000/defectPrediction/single", jsonArray.getJSONObject(i).toJSONString());//发送请求进行缺陷扫描
                if(!postHttpByPath.contains("500 Internal Server Error")){
                    JSONObject jsonObject2 = JSONObject.parseObject(postHttpByPath);
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("filename",jsonArray.getJSONObject(i).getString("filename"));
                    jsonObject1.put("result",JSONArray.parseArray(jsonObject2.getString("res")).get(0));
                    defectResult.add(jsonObject1);
                }
            }

            if (taskScanningScheme.getSqlrule().equals("true")) {//规约扫描
                JSONObject jsonObject =new JSONObject();
                try {
                    jsonObject.putAll(RuleChecker.runByArrary(jsonArray.getJSONObject(i).getString("code"), config));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(jsonObject);
                jsonArray.getJSONObject(i).remove("code");//移除掉代码内容，防止数据库存储内容太庞大
                jsonArray.getJSONObject(i).put("result", jsonObject.clone());//需要将文件按照行数来分割
            }
            taskScanStatus.setStatus(taskScanStatus.getStatus()+jsonArray.getJSONObject(i).getString("filename")+"扫描完成。\n");
            taskScanStatus.setProcess(String.valueOf((i+1)*100/jsonArray.size()));
            writeRedis(taskScanStatus,id);
        }

        TaskScanResult taskScanResult=new TaskScanResult(id, LocalDateTime.now());
        if (taskScanningScheme.getSqlrule().equals("true")) {//规约扫描
            taskScanResult.setJavaruleresult(jsonArray.toJSONString());
            jsonArray.clear();
        }
        JSONArray cloneResult = new JSONArray();
        SetTaskScanStatus(taskScanStatus,"正在进行克隆问题扫描...\n",taskScanStatus.getStatus(),taskScanStatus.getProcess(),"0");
        writeRedis(taskScanStatus,id);

        JSONArray jsonArray4=new JSONArray();
        if (taskScanningScheme.getApproximatecode().equals("true")) {
            for(int k=1;k<=jsonArray3.size();k++){//克隆代码检测
                jsonArray4.add(jsonArray3.get(k-1));
                if(k%8==0||k==jsonArray3.size()){//把每8个方法为单位传到python web进行检测
                    String postHttpByPath = HttpClientUtil.createPostHttpByPath("http://8.134.53.160:5000/cloneDetection/", jsonArray4.toJSONString());

                    if(!postHttpByPath.equals("error")){//如果检测出错误，那么不加入结果
                        JSONArray res = JSONObject.parseObject(postHttpByPath).getJSONArray("res");
                        for (int i = 0; i < res.size(); i++) {
                            JSONArray similar_code_list = res.getJSONObject(i).getJSONArray("similar_code_list");
                            Iterator<Object> iterator =  similar_code_list.iterator();
                            while(iterator.hasNext()){
                                JSONObject next = (JSONObject)iterator.next();
                                Object methodCode = next.getString("methodCode");
                                String methodLines = codeUtil.codeDivideToLineWithStartLine(methodCode.toString(), next.getIntValue("methodLine"));
                                next.put("Code",methodLines);
                            }
                        }
                        cloneResult.addAll(res);
                    }
                    jsonArray4.clear();//清空数组，避免重复
                    taskScanStatus.setProcess1(String.valueOf(k*100/jsonArray3.size()));//写进度到redis
                    writeRedis(taskScanStatus,id);
                }
            }
            SetTaskScanStatus(taskScanStatus,"任务结束。\n",taskScanStatus.getStatus()+"扫描任务完成，请点击右上角关闭此窗口，详情请点击问题列表查看。\n",taskScanStatus.getProcess(),taskScanStatus.getProcess1());
            writeRedis(taskScanStatus,id);
        }else{
            SetTaskScanStatus(taskScanStatus,"本次不进行克隆代码扫描。\n",taskScanStatus.getStatus()+"本次不进行克隆代码扫描。\n扫描任务完成，请点击右上角关闭此窗口，详情请点击问题列表查看。\n",taskScanStatus.getProcess(),"100");
            writeRedis(taskScanStatus,id);
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
            if(taskScanningScheme.getAutomaticemail().equals("true")){
                //构造邮件内容
                Context ctx = new Context();
                ctx.setVariable("timestamp", taskScanResult.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                ctx.setVariable("task", taskManagementTable.getTaskName());
                ctx.setVariable("repository", taskManagementTable.getWareHouse());
                ctx.setVariable("branch", taskManagementTable.getBranch());
                ctx.setVariable("total", 271);
                ctx.setVariable("serious", 271);
                ctx.setVariable("warn", 0);
                ctx.setVariable("propose", 0);
                String mail = templateEngine.process("scanresult.html", ctx);
                String[] emailList=taskScanningScheme.getEmail().substring(1,taskScanningScheme.getEmail().length()-1).split(",");
                for (String s : emailList) {
                    sendHtmlMail.sendHtmlMails("1192129669@qq.com", s, "扫描结果通知", mail);
                }
            }
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

    /**
     * 写redis
     * @param taskScanStatus
     * @param id
     */
    public void writeRedis(TaskScanStatus taskScanStatus,int id){
        try {
            String s = mapper.writeValueAsString(taskScanStatus);
            stringRedisTemplate.opsForValue().set("scanning:"+id,s,30*5, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置扫苗结果
     * @param taskScanStatus
     * @param
     */
    public TaskScanStatus SetTaskScanStatus(TaskScanStatus taskScanStatus,String Scanmsg,String Status,String Process,String Process1){
        taskScanStatus.setScanmsg(Scanmsg);
        taskScanStatus.setStatus(Status);
        taskScanStatus.setProcess(Process);
        taskScanStatus.setProcess1(Process1);
        return taskScanStatus;

    }
}
