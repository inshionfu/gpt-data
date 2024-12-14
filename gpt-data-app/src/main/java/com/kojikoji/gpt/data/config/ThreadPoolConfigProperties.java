package com.kojikoji.gpt.data.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName ThreadPoolConfigProperties
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/12 16:35
 * @Version
 */

@Data
@ConfigurationProperties(prefix = "thread.pool.executor.config", ignoreInvalidFields = true)
public class ThreadPoolConfigProperties {
    /* 核心线程数*/
    private Integer corePoolSize = 20;
    /* 最大线程数*/
    private Integer maxPoolSize = 200;
    /* 最大等待时间*/
    private Long keepAliveTime = 10L;
    /* 最大队列数*/
    private Integer blockingQueueSize = 5000;
    /* 拒绝策略*/
    private String policy = "AbortPolicy";
}
