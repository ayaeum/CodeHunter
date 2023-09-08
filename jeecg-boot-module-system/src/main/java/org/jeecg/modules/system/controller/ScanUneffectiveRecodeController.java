package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.service.IScanUneffectiveRecodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 菜瓜皮
 * @since 2023-04-26
 */
@RestController
@RequestMapping("/scanUneffectiveRecode")
public class ScanUneffectiveRecodeController {

    @Autowired
    private IScanUneffectiveRecodeService iScanUneffectiveRecodeService;

    @PostMapping(value = "/test")
    public Result<String> test(@RequestBody(required = false) JSONObject jsonObject){
        System.out.println(jsonObject.getString("ID"));
        if (iScanUneffectiveRecodeService.rejectRecord(Long.parseLong(jsonObject.getString("ID")))) {
            return new Result<String>(200,"操作成功");
        }
        return new Result<String>(500,"操作失败");
    }
}
