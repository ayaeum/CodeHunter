package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.system.entity.AccessToken;
import org.jeecg.modules.system.mapper.AccessTokenMapper;
import org.jeecg.modules.system.service.IAccessTokenService;
import org.springframework.stereotype.Service;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2022年08月20日 1:15
 */
@Service
public class AccessTokenImpl extends ServiceImpl<AccessTokenMapper, AccessToken> implements IAccessTokenService {
}
