package com.kojikoji.gpt.data.config.chatgpt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName ChatGPTSDKConfigProperties
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/12 16:55
 * @Version
 */

@Data
@ConfigurationProperties(prefix = "gpt.sdk.config", ignoreInvalidFields = true)
public class ChatGPTSDKConfigProperties {
    /* 转发地址*/
    private String apiHost;
    /* apikey*/
    private String apiKey;
    /* token*/
    private String authToken;
}
