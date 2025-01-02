package com.kojikoji.gpt.data.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName AlipayConfigProperties
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/28 15:03
 * @Version
 */
@Data
@ConfigurationProperties(prefix = "alipay", ignoreInvalidFields = true)
public class AlipayConfigProperties {
    private String appId;
    private String merchantPrivateKey;
    private String gatewayUrl;
    private String format;
    private String charset;
    private String alipayPublicKey;
    private String signType;
    private String notifyUrl;
    private String returnUrl;
}
