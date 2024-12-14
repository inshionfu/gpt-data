package com.kojikoji.gpt.data.config.glm;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName GLMSDKConfigProperties
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/12 20:04
 * @Version
 */

@Data
@ConfigurationProperties(prefix = "glm.sdk.config", ignoreInvalidFields = true)
public class GLMSDKConfigProperties {
    private String apiHost;
    private String apiKey;
}
