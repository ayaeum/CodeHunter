package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.checkerframework.checker.units.qual.C;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.controller.Gitee.ReposController;
import org.jeecg.modules.system.entity.CertificationManagementForm;
import org.jeecg.modules.system.entity.TaskDatasTable;
import org.jeecg.modules.system.model.Base64Decode;
import org.jeecg.modules.system.service.ICertificationManagementFormService;
import org.jeecg.modules.system.service.ITaskDatasTableService;
import org.jeecg.modules.system.service.ITaskManagementTableService;
import org.jeecg.modules.system.util.DocumentProcessingUtil.CodeCutting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 菜瓜皮
 * @since 2022-09-18
 */
@RestController
@RequestMapping("/taskDatasTable")
public class TaskDatasTableController {

    @Autowired
    private ITaskManagementTableService iTaskManagementTableService;

    @Autowired
    private ICertificationManagementFormService iCertificationManagementFormService;

    @Autowired
    private ITaskDatasTableService iTaskDatasTableService;

    /**
     *调用检测方法
     * @param jsonObject
     * @return 返回检测数据
     */
//    @PostMapping(value = "/detection")
//    public Result<List<JSONArray>> detection(@RequestBody JSONObject jsonObject) {
//        //先查出主体，根据主体来判断平台（根据主体标识名来找出平台信息）
//        CertificationManagementForm certificationManagementForm = iCertificationManagementFormService.selectOneCer(jsonObject.getString("jeecg_account"),jsonObject.getString("repoowner"));
//
//        String path = null;
//        Result<List<JSONArray>> res = new Result<List<JSONArray>>();
//        ArrayList<String[]> filearray = new ArrayList<String[]>();
//        ArrayList<String> filepath = new ArrayList<String>();
//        List<JSONArray> resultcarry = new ArrayList<JSONArray>();
//        JSONArray code = new JSONArray();
//        JSONArray rule_res = new JSONArray();
//
//        /**
//         *
//         */
//        if(certificationManagementForm.getPlatform().equals("Gitee")){
//
//            String accessToken = iCertificationManagementFormService.queryCerAccessTokenBySysnameandIdentificationName(jsonObject.getString("jeecg_account"),iTaskManagementTableService.QueryTaskOwner(jsonObject.getString("jeecg_account"),jsonObject.getString("taskName")));
//            Iterator iterator = jsonObject.getJSONArray("oncheckFile").iterator();
//            while(iterator.hasNext()){
//                path = iterator.next().toString();
//                if(!path.endsWith(".java")){
//                    iterator.remove();
//                }else{
//                    String base64code = ReposController.getPathContents(accessToken,jsonObject.getString("repoowner"),jsonObject.getString("repo"),path).getString("content");
//                    try {
////                        resultcarry.add(run.runByString(Base64Decode.decode(base64code)));
//                        JSONArray jsonArray1 = new JSONArray();
//                        JSONArray jsonArray2 = run.runByString(Base64Decode.decode(base64code));
////                        jsonArray2.add(path);
////                        jsonArray1.add(jsonArray2);
//                        JSONObject jsonObject1 = new JSONObject();
//                        jsonObject1.put("qlist",jsonArray2);
//                        jsonArray1.add(jsonObject1);
//                        JSONObject jsonObject2 = new JSONObject();
//                        jsonObject1.put("filename",path);
//                        if(jsonArray2.size()>15){
//                            jsonObject1.put("level","严重");
//                            jsonObject1.put("color","red");
//                            jsonObject1.put("description","发现"+jsonArray2.size()+"处不规范代码");
//                        }else{
//                            if(jsonArray2.size()>5){
//                                jsonObject1.put("level","警告");
//                                jsonObject1.put("color","yellow");
//                                jsonObject1.put("description","发现"+jsonArray2.size()+"处不规范代码");
//                            }else{
//                                jsonObject1.put("level","建议");
//                                jsonObject1.put("color","green");
//                                jsonObject1.put("description","发现"+jsonArray2.size()+"处不规范代码");
//                            }
//                        }
//                        jsonArray1.add(jsonObject2);
//
//                        rule_res.add(jsonArray1);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    filearray.add(CodeCutting.codecutting(base64code));
//                    filepath.add(path);
//                    JSONObject codecarry = new JSONObject();
//                    codecarry.put("path",path);
//                    codecarry.put("code",CodeCutting.codecutting(base64code));
//                    code.add(codecarry);
//                }
//            }
//            resultcarry.add(code);
//        }else{
//            ;
//        }
//
//        try{
//            codeDetection codeDetection = new codeDetection();
//            List<JSONArray> testing_res = codeDetection.detectionCode(jsonObject.getString("jeecg_account"),jsonObject.getString("taskName"), preprocess.getCodes(filearray, filepath),jsonObject.getJSONArray("oncheckFile"));
//            //将检测结果加入数据库
//            TaskDatasTable taskDatasTable = new TaskDatasTable(jsonObject.getString("jeecg_account"),jsonObject.getString("taskName"),0,Integer.valueOf(testing_res.get(1).getJSONObject(0).getString("clone_serious")),Integer.valueOf(testing_res.get(1).getJSONObject(0).getString("clone_warning")),Integer.valueOf(testing_res.get(1).getJSONObject(0).getString("clone_proposal")));
//            iTaskDatasTableService.insertTaskDatasTableService(jsonObject.getString("jeecg_account"),jsonObject.getString("taskName"),taskDatasTable);
//            testing_res.add(iTaskDatasTableService.queryTaskDatasTable_JSONArray(jsonObject.getString("jeecg_account"),jsonObject.getString("taskName")));
////            testing_res.get(0).addAll(rule_res);
//            resultcarry.addAll(testing_res);
//            resultcarry.add(rule_res);
//            res.setResult(resultcarry);
//            res.setCode(200);
//            res.setMessage("OK");
//        }catch (Exception e){
//            System.out.println(e);
//        }
//        return res;
//    }
}
