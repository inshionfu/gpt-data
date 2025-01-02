package com.kojikoji.gpt.data.config;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName AlipayConfig
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/28 15:03
 * @Version
 */

@Slf4j
@Configuration
@EnableConfigurationProperties(AlipayConfigProperties.class)
public class AlipayConfig {

    @Bean(name = "alipayClient")
    @ConditionalOnProperty(value = "alipay.enabled", havingValue = "true", matchIfMissing = false)
    public AlipayClient alipayClient(AlipayConfigProperties properties) {
        log.info("notifyUrl : {}", properties.getNotifyUrl());
        return new DefaultAlipayClient(properties.getGatewayUrl(),
                properties.getAppId(),
                properties.getMerchantPrivateKey(),
                properties.getFormat(),
                properties.getCharset(),
                properties.getAlipayPublicKey(),
                properties.getSignType());
    }

}
