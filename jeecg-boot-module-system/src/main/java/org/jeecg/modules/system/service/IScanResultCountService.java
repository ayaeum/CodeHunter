package org.jeecg.modules.system.service;

import org.jeecg.modules.system.entity.ScanResultCount;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 菜瓜皮
 * @since 2023-05-15
 */
public interface IScanResultCountService extends IService<ScanResultCount> {

    /**
     * 增加扫描结果问题数
     * @param count
     * @return
     */
    public boolean addScanResultCount(long count);
}
