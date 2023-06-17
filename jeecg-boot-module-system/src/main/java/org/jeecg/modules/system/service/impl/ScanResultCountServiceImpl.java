package org.jeecg.modules.system.service.impl;

import org.jeecg.modules.system.entity.ScanResultCount;
import org.jeecg.modules.system.mapper.ScanResultCountMapper;
import org.jeecg.modules.system.mapper.ScanUneffectiveRecodeMapper;
import org.jeecg.modules.system.service.IScanResultCountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 菜瓜皮
 * @since 2023-05-15
 */
@Service
public class ScanResultCountServiceImpl extends ServiceImpl<ScanResultCountMapper, ScanResultCount> implements IScanResultCountService {

    @Autowired
    private ScanResultCountMapper scanResultCountMapper;

    /**
     * 增加扫描结果问题数
     * @param count
     * @return
     */
    public boolean addScanResultCount(long count){
        ScanResultCount scanResultCount=new ScanResultCount();
        scanResultCount.setCount(count);
        return scanResultCountMapper.insert(scanResultCount)==1;
    }
}
