package org.jeecg.modules.system.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2022年10月23日 10:17
 */
@Data
public class RedisData {
    private LocalDateTime expireTime;
    private Object data;
}
