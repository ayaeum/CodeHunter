package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.system.entity.RuleJavaStatute;
import org.jeecg.modules.system.mapper.RuleJavaStatuteMapper;
import org.jeecg.modules.system.service.IRuleJavaStatuteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 菜瓜皮
 * @since 2023-02-16
 */
@Service
public class RuleJavaStatuteServiceImpl extends ServiceImpl<RuleJavaStatuteMapper, RuleJavaStatute> implements IRuleJavaStatuteService {
    @Resource
    private RuleJavaStatuteMapper ruleJavaStatuteMapper;

    public List<RuleJavaStatute> GetRule(){
        return ruleJavaStatuteMapper.selectList(null);
    }

    public List<RuleJavaStatute> GetRuleIDandRemark(){
        QueryWrapper<RuleJavaStatute> qw=new QueryWrapper<>();
        qw.select("id","remark");
        return ruleJavaStatuteMapper.selectList(qw);
    }
}
