package com.kojikoji.gpt.data.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName GoogleGuavaCodeCacheConfig
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/15 17:20
 * @Version
 */
@Configuration
public class GoogleGuavaCodeCacheConfig {

    @Bean(name = "codeCache")
    public Cache<String, String> codeCache() {
        return CacheBuilder.newBuilder()
                .expireAfterAccess(3, TimeUnit.MINUTES)
                .build();
    }
}
