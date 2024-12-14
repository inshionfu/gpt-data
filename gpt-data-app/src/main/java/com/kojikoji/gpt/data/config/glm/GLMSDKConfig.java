package com.kojikoji.gpt.data.config.glm;

import cn.bugstack.chatglm.session.OpenAiSession;
import cn.bugstack.chatglm.session.defaults.DefaultOpenAiSessionFactory;
import com.kojikoji.gpt.data.config.chatgpt.ChatGPTSDKConfigProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName GLMSDKConfig
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/12 19:40
 * @Version
 */

@Configuration
@EnableConfigurationProperties(GLMSDKConfigProperties.class)
public class GLMSDKConfig {
    @Bean
    public OpenAiSession openAiSession(GLMSDKConfigProperties properties) {
        // 1.配置文件
        cn.bugstack.chatglm.session.Configuration configuration = new cn.bugstack.chatglm.session.Configuration();
        configuration.setApiHost(properties.getApiHost());
        configuration.setApiSecretKey(properties.getApiKey());
        // 2.会话工厂
        DefaultOpenAiSessionFactory factory = new DefaultOpenAiSessionFactory(configuration);

        // 3.开启会话
        return factory.openSession();
    }
}
