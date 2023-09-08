package org.jeecg.modules.system.controller.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microsoft.schemas.office.x2006.keyEncryptor.certificate.impl.CTCertificateKeyEncryptorImpl;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSONArray;
import org.checkerframework.checker.units.qual.A;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.controller.CertificationManagementFormController;
import org.jeecg.modules.system.controller.Gitee.ReposController;
import org.jeecg.modules.system.controller.TaskManagementTableController;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.service.*;
import org.jeecg.modules.system.service.impl.TaskManagementTableServiceImpl;
import org.jeecg.modules.system.util.DocumentProcessingUtil.CodeCutting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
//import main.run;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2022年08月19日 19:30
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class test {

    @Autowired
    private ITaskManagementTableService iTaskManagementTableService;

    @Autowired
    private ICertificationManagementFormService iCertificationManagementFormService;

    @Autowired
    private ITaskDatasTableService iTaskDatasTableService;

    @Autowired
    private ITaskDetailsTableService iTaskDetailsTableService;

    @Autowired
    private ITaskPathTableService iTaskPathTableService;

    @GetMapping(value = "/demo1")
    public static String demo1(@RequestParam("jeecg_account")String jeecg_account,@RequestParam("task")String task) {
//        JSONArray result = null;
//        JSONArray conditionList = JSONArray.parseArray(oncheckFile);
//        Iterator iter = conditionList.iterator();
//        while(iter.hasNext()){
//            System.out.println(iter.next());
//        }
//        try{
//            result = codeDetection.detectionCode(preprocess.getCodes());
//            System.out.println(result);
//            System.out.println("运行完成");
//        }catch (Exception e){
//            System.out.println(e);
//        }
//
//        return result;

        System.out.println(jeecg_account);
        JSONObject taskJSON=(JSONObject) JSONObject.parse(task);
        //查出主体信息，调用代码平台接口获取仓库文件列表

        return jeecg_account;
    }

    /**
     *
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "/demo2")
    public Result<List<JSONArray>> demo2(@RequestBody JSONObject jsonObject) {
        Result<List<JSONArray>> res = new Result<List<JSONArray>>();
        List<JSONArray> resresult = new ArrayList<JSONArray>();
        JSONArray code = new JSONArray();
        JSONArray oncheckFile = new JSONArray();
        System.out.println(jsonObject);
        //拿到该任务的认证信息
        String access_token = iCertificationManagementFormService.queryCerAccessTokenBySysnameandIdentificationName(jsonObject.getString("jeecg_account"),iTaskManagementTableService.QueryTaskOwner(jsonObject.getString("jeecg_account"),jsonObject.getString("taskName")));

        //查询问题列表
        JSONArray taskDetailsList = iTaskDetailsTableService.queryAllTaskDetailsBySysUserNameAndTaskName(jsonObject.getString("jeecg_account"),jsonObject.getString("taskName"));

        //循环列表,拉取代码并构建代码数组
        TaskPathTable taskPathTable = iTaskPathTableService.queryTaskPathBySysnameandTaskName(jsonObject.getString("jeecg_account"),jsonObject.getString("taskName"));
        JSONArray jsonArray = JSONArray.parseArray(taskPathTable.getPathSet());
        oncheckFile.add(taskPathTable.getOncheckFile());
        Iterator iter = jsonArray.iterator();
        while(iter.hasNext()){
            String path = iter.next().toString();
            String base64code = ReposController.getPathContents(access_token,jsonObject.getString("owner"),jsonObject.getString("repo"),path).getString("content");
            JSONObject codecarry = new JSONObject();
            codecarry.put("path",path);
            codecarry.put("code",CodeCutting.codecutting(base64code));
            code.add(codecarry);
        }
        resresult.add(code);
        resresult.add(taskDetailsList);
        JSONArray DatasArray = iTaskDatasTableService.queryTaskDatasTable_JSONArray(jsonObject.getString("jeecg_account"),jsonObject.getString("taskName"));
        JSONArray LastData = new JSONArray();
        LastData.add(DatasArray.get(DatasArray.size()-1));
        resresult.add(LastData);
        resresult.add(DatasArray);
        resresult.add(oncheckFile);
        res.setCode(200);
        res.setMessage("查询成功");
        res.setResult(resresult);
        return res;
    }
}
