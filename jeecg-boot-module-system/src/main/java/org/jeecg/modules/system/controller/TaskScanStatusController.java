package org.jeecg.modules.system.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import detector.RuleChecker;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.jmreport.desreport.service.IJimuReportShareService;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.model.HttpClientUtil;
import org.jeecg.modules.system.service.*;
import org.jeecg.modules.system.util.CodeUtil;
import org.jeecg.modules.system.util.MethodExtractorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

    @Autowired
    private IScanUneffectiveRecodeService iScanUneffectiveRecodeService;

    @Autowired
    private IRuleJavaStatuteService iRuleJavaStatuteService;

    @Autowired
    private IScanResultCountService iScanResultCountService;

    @GetMapping(value = "StartScanApi")
    public Result<String> StartScanApi(){

        return null;
    }

    /**
     * 网络钩子自动检测
     */
    @PostMapping(value = "/ConfigurableStart")
    public Result<String> ConfigurableStart(@RequestBody(required = false) JSONObject jsonObject){
        //如果RequestBody为空，返回错误提示
        if(Objects.isNull(jsonObject)){
            return new Result<String>(400,"错误的请求格式：参数为空");
        }

        String mergelink = jsonObject.getString("mergelink");
        String substring = mergelink.substring(0,mergelink.indexOf("pull-requests") + ("pull-requests").length());
        String getHttpByPath = HttpClientUtil.createGetHttpByPathWithBasic(substring);
        JSONObject jsonObject3 = JSONObject.parseObject(getHttpByPath);
        JSONArray values = jsonObject3.getJSONArray("values");

        JSONArray jsonArray=new JSONArray();

        for (int i = 0; i < values.size(); i++) {
            //diff链接
            String difflink = values.getJSONObject(i).getJSONObject("links").getJSONArray("self").getJSONObject(0).getString("href");
            int projects = difflink.indexOf("projects");
            difflink=difflink.substring(0,projects-1)+"/rest/api/1.0"+difflink.substring(projects-1)+"/diff";
            JSONObject diffresult = JSONObject.parseObject(HttpClientUtil.createGetHttpByPathWithBasic(difflink));
            JSONArray diffs = diffresult.getJSONArray("diffs");
            //仓库链接
            String repolink = values.getJSONObject(i).getJSONObject("toRef").getJSONObject("repository").getJSONObject("links").getJSONArray("self").getJSONObject(0).getString("href");
            repolink=repolink.substring(0,projects-1)+"/rest/api/1.0"+repolink.substring(projects-1);

            for (int i1 = 0; i1 < diffs.size(); i1++) {
                //文件列表
                JSONObject fileList=new JSONObject();
                String string = diffs.getJSONObject(i1).getJSONObject("destination").getString("toString");
                if (string.endsWith(".java")) {//非java文件不加入
                    fileList.put("filename",string.substring(string.lastIndexOf("/")+1));
                    fileList.put("filepath",string);
                    fileList.put("link",repolink+"/"+string);
                    jsonArray.add(fileList);
                }
            }
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            //获取文件内容
            String getHttpByPathWithBasic = HttpClientUtil.createGetHttpByPathWithBasic(jsonArray.getJSONObject(i).getString("link"));
            JSONObject code = JSONObject.parseObject(getHttpByPathWithBasic);
            JSONArray lines = code.getJSONArray("lines");
            String codestr="";
            for (int i1 = 0; i1 < lines.size(); i1++) {
                codestr+=lines.getJSONObject(i1).getString("text")+"\n";
            }
            jsonArray.getJSONObject(i).put("code",codestr);
        }

        //拉取配置问题
        List<RuleJavaStatute> ruleJavaStatutes = iRuleJavaStatuteService.GetRuleIDandRemark();
        ListIterator<RuleJavaStatute> ruleJavaStatuteListIterator = ruleJavaStatutes.listIterator();
        JSONObject jsonObject1=new JSONObject();

        //构造配置表
        while(ruleJavaStatuteListIterator.hasNext()){
            RuleJavaStatute next = ruleJavaStatuteListIterator.next();
            jsonObject1.put(next.getId().toString(),next.getRemark());
        }

        //将自定义配置加到配置表
        JSONObject configurable = jsonObject.getJSONObject("configurable");
        Iterator<Map.Entry<String, Object>> iterator = configurable.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, Object> next = iterator.next();
            jsonObject1.replace(next.getKey(),next.getValue());
        }

        for (int i = 0; i < jsonArray.size()/2; i++) {
            JSONObject file = jsonArray.getJSONObject(i);
            JSONObject result =new JSONObject();
            //调用检测
            result.putAll(RuleChecker.runByArrary(file.getString("code"), jsonObject1));
            file.put("result",result);
        }

        //把这些数据装到静态页面里边去
        long count=0;
        List<List<String>> list=new LinkedList<List<String>>();
        //调用检测
        JSONObject jsonObject2 = RuleChecker.runByArrary(javacode, jsonObject1);

        for(String str:jsonObject2.keySet()){
            JSONArray result = jsonObject2.getJSONObject(str).getJSONArray("result");
            count+=result.size();
            for (int i = 0; i < result.size(); i++) {
                List<String> list1=new LinkedList<String>();
                list1.add(result.getJSONObject(i).getString("ID"));
                list1.add("filename");
                list1.add("filepath");
                list1.add(result.getJSONObject(i).getString("questionableLine"));
                list1.add("规约描述规约描述规约描述规约描述规约描述规约描述");
                list1.add(result.getJSONObject(i).getString("errorCode"));
                list1.add("按钮");
                list.add(list1);
            }
        }

        FileWriter writer = null;
        String filePath="C:\\AICodeHunter\\wensScanResult.html";
        if (iScanResultCountService.addScanResultCount(count)) {
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

    @GetMapping(value = "/StartScan")
    public Result<JSONArray> StartScan(int id) throws IOException {
        if (readRedis(id)) {
            return Result.error("任务一存在，请勿重复开启扫描");
        }
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

        for(int i=0;i<jsonArray.size();i++){        //JAVA规约扫描、代码缺陷检测
            try{
                CompilationUnit cu = StaticJavaParser.parse(jsonArray.getJSONObject(i).getString("code"));//生成相似度检测需要的方法列表
                new MethodExtractorUtil.MethodVisitor().visit(cu, null);
                jsonArray3.addAll(new MethodExtractorUtil.MethodVisitor().methodCutter(cu, jsonArray.getJSONObject(i).getString("filename")));//分割方法
            }catch (IOException e) {
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
                jsonObject.putAll(RuleChecker.runByArrary(jsonArray.getJSONObject(i).getString("code"), config));
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
                    sendHtmlMail.sendHtmlMails("2861446127@qq.com", s, "扫描结果通知", mail);
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

    /**
     * 查询扫描进度
     * @param id
     * @return
     */
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
     * 查redis
     * @param id
     */
    public boolean readRedis(int id){
        return stringRedisTemplate.opsForValue().get("scanning:" + id) != null;
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

    public static String javacode="package org.jeecg.modules.system.controller;\n\nimport com.alibaba.fastjson.JSON;\nimport com.alibaba.fastjson.JSONArray;\nimport com.alibaba.fastjson.JSONObject;\nimport com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;\nimport com.fasterxml.jackson.core.JsonProcessingException;\nimport com.fasterxml.jackson.databind.ObjectMapper;\nimport lombok.extern.slf4j.Slf4j;\nimport org.jeecg.common.api.vo.Result;\nimport org.jeecg.modules.system.controller.Gitee.gitController;\nimport org.jeecg.modules.system.entity.CertificationManagementForm;\nimport org.jeecg.modules.system.entity.MailTemplate;\nimport org.jeecg.modules.system.model.HttpClientUtil;\nimport org.jeecg.modules.system.service.ICertificationManagementFormService;\nimport org.jeecg.modules.system.service.IGitlabService;\nimport org.jeecg.modules.system.service.ISendMailService;\nimport org.jeecg.modules.system.util.RSAutil;\nimport org.springframework.beans.factory.annotation.Autowired;\nimport org.springframework.data.redis.core.StringRedisTemplate;\nimport org.springframework.web.bind.annotation.GetMapping;\nimport org.springframework.web.bind.annotation.RequestMapping;\nimport org.springframework.web.bind.annotation.RequestParam;\nimport org.springframework.web.bind.annotation.RestController;\nimport org.jeecg.modules.system.controller.Test.Enum;\n\nimport java.util.Date;\nimport java.util.Iterator;\nimport java.util.LinkedList;\nimport java.util.List;\n\nimport org.jeecg.modules.system.controller.GitBypwd.connect;\n\n/**\n * <p>\n *  前端控制器\n * </p>\n *\n * @author 菜瓜皮\n * @since 2022-08-29\n */\n@Slf4j\n@RestController\n@RequestMapping(\"/certificationManagementForm\")\npublic class CertificationManagementFormController {\n\n    @Autowired\n    private ICertificationManagementFormService iCertificationManagementFormService;\n\n    @Autowired\n    private StringRedisTemplate stringRedisTemplate;\n\n    private static final ObjectMapper mapper = new ObjectMapper();\n\n    @Autowired\n    private ISendMailService iSendMailService;\n\n    @Autowired\n    private IGitlabService iGitlabService;\n\n    /**\n     * 新增认证，若存在不予添加，不存在则加之\n     * @param certificationManagementForm\n     * @return\n     */\n    @GetMapping(value = \"/addCertification\")\n    public Result<List<CertificationManagementForm>> addCertification(CertificationManagementForm certificationManagementForm){\n        Result<List<CertificationManagementForm>> resultobj = new Result<List<CertificationManagementForm>>();\n        //1.首先判断认证是否存在(根据系统账号、平台、账号、标识名)\n        if(iCertificationManagementFormService.Cer_Exist(certificationManagementForm)){\n            resultobj.setMessage(\"认证重复\");\n            return resultobj;\n        }\n\n        if(iCertificationManagementFormService.IdentificationName_Exist(certificationManagementForm)){\n            resultobj.setMessage(\"认证标识名已存在\");\n            return resultobj;\n        }\n        certificationManagementForm.setDatatime((new Date().getTime())/1000);\n        switch (certificationManagementForm.getPlatform()){\n            case \"GitLab\":\n                if(iGitlabService.getGitLabConnect(certificationManagementForm.getGiteeAccount(), RSAutil.decrypt(certificationManagementForm.getGiteePassword(), RSAutil.PrivateKey))){\n                    resultobj.setMessage(Enum.ADD_SUCCESS);\n                    certificationManagementForm.setAccessToken(RSAutil.decrypt(certificationManagementForm.getGiteePassword(), RSAutil.PrivateKey));\n                    iCertificationManagementFormService.InsertCer(certificationManagementForm);\n                    resultobj.setResult(iCertificationManagementFormService.queryCerBySysUserName(certificationManagementForm.getSysUsername()));\n                }\n                break;\n            case \"Gitee\":\n                Result<JSONObject> result = connect.gitCer(certificationManagementForm.getSysUsername(),certificationManagementForm.getGiteeAccount(),certificationManagementForm.getGiteePassword(),certificationManagementForm.getClientId(),certificationManagementForm.getClientSecret());\n                if(result.getMessage().equals(Enum.GITEE_SIGNIN_SUCCESS)){\n                    iSendMailService.SendMail(new MailTemplate(\"1192129669@qq.com\",certificationManagementForm.getGiteeAccount(),\"CodeHunter邮件通知\",\"尊敬的用户，您的码云账号\"+certificationManagementForm.getGiteeAccount()+\"已授权CodeHunter平台，感谢使用CodeHunter平台。如非本人操作，请立即更改密码。\"));\n                    certificationManagementForm.setDatatime(Long.parseLong(result.getResult().getString(\"created_at\")));\n                    certificationManagementForm.setAccessToken(result.getResult().getString(\"access_token\"));\n                    iCertificationManagementFormService.InsertCer(certificationManagementForm);\n                    resultobj.setCode(Enum.REQUEST_NORMAL);\n                    resultobj.setMessage(Enum.ADD_SUCCESS);\n                    resultobj.setResult(iCertificationManagementFormService.queryCerBySysUserName(certificationManagementForm.getSysUsername()));\n                }\n                break;\n            case \"GitHub\":\n                //先验证秘钥\n\n                //若秘钥通过，则存入数据库\n                System.out.println(\"GitHub\");\n                break;\n        }\n//        iCertificationManagementFormService.Update_Redis(certificationManagementForm.getSysUsername());\n        resultobj.setMessage(\"添加成功\");\n        return resultobj;\n    }\n\n    /**\n     * 根据系统用户名与码云邮箱进行认证查询\n     * @param jeecg_account\n     * @param giteeAccount\n     * @return\n     */\n    @GetMapping(value = \"/queryCertification\")\n    public Result<CertificationManagementForm> queryCertification(@RequestParam(\"jeecg_account\")String jeecg_account, @RequestParam(\"giteeAccount\")String giteeAccount){\n        Result<CertificationManagementForm> resultobj = new Result<CertificationManagementForm>();\n        QueryWrapper<CertificationManagementForm> qw = new QueryWrapper<CertificationManagementForm>();\n        qw.eq(\"sysUsername\",jeecg_account).eq(\"giteeAccount\",giteeAccount);\n        List<CertificationManagementForm> result = iCertificationManagementFormService.list(qw);\n\n//        try {\n//            String redis_key = mapper.writeValueAsString(result);\n//            System.out.println(redis_key);\n//        } catch (JsonProcessingException e) {\n//            e.printStackTrace();\n//        }\n\n        if(result.size()==0){\n            resultobj.setCode(Enum.REQUEST_NORMAL);\n            resultobj.setMessage(Enum.QUERY_NULL);\n            resultobj.setResult(null);\n        }\n        else{\n            resultobj.setCode(Enum.REQUEST_NORMAL);\n            resultobj.setMessage(Enum.AUTHENTICATION_EXIST);\n            resultobj.setResult(result.get(0));\n        }\n        return resultobj;\n    }\n\n    @GetMapping(value = \"/queryCertificationBySysName\")\n    public Result<List<CertificationManagementForm>> queryCertificationBySysName(@RequestParam(\"jeecg_account\")String jeecg_account){\n        Result<List<CertificationManagementForm>> resultobj = new Result<List<CertificationManagementForm>>();\n\n        List<CertificationManagementForm> result = iCertificationManagementFormService.queryCerBySysUserName(jeecg_account);\n\n//        if(stringRedisTemplate.opsForValue().get(\"Certification:\"+jeecg_account) != null){\n//            result = (List)JSONArray.parseArray(stringRedisTemplate.opsForValue().get(\"Certification:\"+jeecg_account));\n//        }else{\n//            iCertificationManagementFormService.Update_Redis(jeecg_account);\n//        }\n        resultobj.setMessage(Enum.QUERY_SUCCESS);\n        resultobj.setCode(Enum.REQUEST_NORMAL);\n        resultobj.setResult(result);\n        return resultobj;\n    }\n\n\n    @GetMapping(value = \"/deleteCertificationBySysNameandGitee\")\n    public Result<List<CertificationManagementForm>> deleteCertificationBySysNameandGitee(@RequestParam(\"jeecg_account\")String jeecg_account,@RequestParam(\"giteeAccount\")String giteeAccount){\n        Result<List<CertificationManagementForm>> resultobj = new Result<List<CertificationManagementForm>>();\n        List<CertificationManagementForm> result = null;\n        QueryWrapper<CertificationManagementForm> qw = new QueryWrapper<CertificationManagementForm>();\n        qw.eq(\"sysUsername\",jeecg_account).eq(\"giteeAccount\",giteeAccount);\n        if(!iCertificationManagementFormService.queryCerIsExistBySysnameandGitee(jeecg_account,giteeAccount)){\n            resultobj.setCode(Enum.REQUEST_NORMAL);\n            resultobj.setMessage(Enum.DELETE_FAILED+Enum.CERTIFICATION_NOT_EXIST);\n            resultobj.setResult(iCertificationManagementFormService.queryCerBySysUserName(jeecg_account));\n        }\n        else{\n            resultobj.setCode(Enum.REQUEST_NORMAL);\n            resultobj.setMessage(Enum.DELETE_SUCCESS);\n            iCertificationManagementFormService.DeleteCer(jeecg_account,giteeAccount);\n//            iCertificationManagementFormService.Update_Redis(jeecg_account);\n            resultobj.setResult(iCertificationManagementFormService.queryCerBySysUserName(jeecg_account));\n        }\n        return resultobj;\n    }\n\n\n    /**\n     * 通过系统账号获取拥有的认证主体、主体仓库、仓库分支信息\n     * @param jeecg_account 系统用户账号\n     * @return 列表\n     */\n    @GetMapping(value = \"/queryCerandRepoBySysName\")\n    public Result<List<JSONObject>> queryCerandRepoBySysName(@RequestParam(\"jeecg_account\")String jeecg_account){\n\n        Result<List<JSONObject>> result = new Result<List<JSONObject>>();\n        List<CertificationManagementForm> CerList = iCertificationManagementFormService.queryCerBySysUserName(jeecg_account);\n        List<JSONObject> list = new LinkedList<JSONObject>();\n\n        Iterator<CertificationManagementForm> iter = CerList.listIterator();\n        while(iter.hasNext()){\n            JSONObject obj = new JSONObject();\n            CertificationManagementForm carry = iter.next();\n            obj.put(\"identificationName\",carry.getIdentificationName());\n            obj.put(\"platform\",carry.getPlatform());\n\n            if(carry.getPlatform().equals(\"Gitee\")){\n                //认证检查\n                if(new Date().getTime()-carry.getDatatime()>86400){\n                    JSONObject result2 = connect.updateAccessToken(carry);\n                    if(result2.getString(\"error\") != null){\n                        result.setCode(400);\n                        result.setMessage(result2.getString(\"error\"));\n                        result.setResult(null);\n                    }\n                    else{\n                        //将认证信息更新到数据库\n                        carry.setAccessToken(result2.getString(\"access_token\"));\n                        carry.setDatatime(result2.getLong(\"created_at\"));\n                        iCertificationManagementFormService.updateById(carry);\n                    }\n                }\n                //获取仓库分支信息\n                int i;\n                JSONArray objrepo = gitController.getAllRepos(carry.getAccessToken());\n                for(i=0;i<objrepo.size();i++) {\n                    //直接拼装path请求分支信息\n                    String s = HttpClientUtil.createGetHttpByPath(objrepo.getJSONObject(i).getString(\"branches_url\").replaceAll(\"\\\\{/branch}\",\"\")+\"?access_token=\"+carry.getAccessToken());\n                    objrepo.getJSONObject(i).put(\"branchs\",JSONArray.parseArray(s));\n                }\n                obj.put(\"repo\",objrepo);\n            }else{\n                if(carry.getPlatform().equals(\"GitLab\")){\n                    //先拿到认证的所有仓库信息\n                    String s = iGitlabService.getUserProjects(carry.getGiteeAccount(),carry.getAccessToken());\n                    JSONArray array = JSON.parseArray(s);\n                    JSONArray gitlabrepo = new JSONArray();\n                    for(int i=0;i<array.size();i++){\n                        String str = iGitlabService.getRepoBranchs(carry.getGiteeAccount(),array.getJSONObject(i).getString(\"id\"),carry.getAccessToken());\n                        JSONArray strjson = JSON.parseArray(str);\n                        array.getJSONObject(i).put(\"branchs\",strjson);\n                        gitlabrepo.add(array.getJSONObject(i));\n                    }\n                    obj.put(\"repo\",gitlabrepo);\n                }\n            }\n            list.add(obj);\n        }\n        result.setResult(list);\n        return result;\n    }\n}\n";

}


