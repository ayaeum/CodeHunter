package org.jeecg.modules.system.service;

import org.jeecg.modules.system.entity.ScanUneffectiveRecode;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 菜瓜皮
 * @since 2023-04-26
 */
public interface IScanUneffectiveRecodeService extends IService<ScanUneffectiveRecode> {
    public boolean addRecord(Long peoblemid);

    /**
     * 拒绝检测结果
     * @param peoblemid
     * @return
     */
    public boolean rejectRecord(long peoblemid);
}
