package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.system.entity.ScanUneffectiveRecode;
import org.jeecg.modules.system.mapper.ScanUneffectiveRecodeMapper;
import org.jeecg.modules.system.service.IScanUneffectiveRecodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 菜瓜皮
 * @since 2023-04-26
 */
@Service
public class ScanUneffectiveRecodeServiceImpl extends ServiceImpl<ScanUneffectiveRecodeMapper, ScanUneffectiveRecode> implements IScanUneffectiveRecodeService {
    @Resource
    private ScanUneffectiveRecodeMapper scanUneffectiveRecodeMapper;

    public boolean addRecord(Long peoblemid){
        ScanUneffectiveRecode scanUneffectiveRecode=new ScanUneffectiveRecode();
        scanUneffectiveRecode.setCount(1);
        scanUneffectiveRecode.setProblemid(peoblemid);
        return scanUneffectiveRecodeMapper.insert(scanUneffectiveRecode)==1;
    }

    /**
     * 拒绝检测结果
     * @param problemid
     * @return
     */
    public boolean rejectRecord(long problemid){
        //先去查数据库，查看id存不存在,不存在则接受，存在则拒绝
        QueryWrapper<ScanUneffectiveRecode> qw=new QueryWrapper<>();
        qw.eq("problemid",problemid).select("id");
        return scanUneffectiveRecodeMapper.selectCount(qw) == 0 && addRecord(problemid);
    }
}
