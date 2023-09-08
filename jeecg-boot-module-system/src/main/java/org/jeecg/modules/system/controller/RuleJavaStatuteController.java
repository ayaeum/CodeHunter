package org.jeecg.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.controller.Test.Enum;
import org.jeecg.modules.system.entity.RuleJavaStatute;
import org.jeecg.modules.system.service.IRuleJavaStatuteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 菜瓜皮
 * @since 2023-02-16
 */
@Slf4j
@RestController
@RequestMapping("/ProtocolConfiguration")
public class RuleJavaStatuteController {
    @Autowired
    private IRuleJavaStatuteService iRuleJavaStatuteService;

    @GetMapping(value = "/getJavaRule")
    public Result<List<RuleJavaStatute>> getJavaRule(){
        Result<List<RuleJavaStatute>> resultobj = new Result<List<RuleJavaStatute>>();
        resultobj.setCode(Enum.REQUEST_NORMAL);
        resultobj.setMessage(Enum.QUERY_SUCCESS);
        resultobj.setResult(iRuleJavaStatuteService.GetRule());
        return resultobj;
    }
}
