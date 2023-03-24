package org.jeecg.modules.system.service;

import org.jeecg.modules.system.entity.RuleJavaStatute;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.TaskScanningScheme;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 菜瓜皮
 * @since 2023-02-16
 */
public interface IRuleJavaStatuteService extends IService<RuleJavaStatute> {
    public List<RuleJavaStatute> GetRule();
}
