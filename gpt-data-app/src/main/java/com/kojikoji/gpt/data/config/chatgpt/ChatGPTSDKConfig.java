package com.kojikoji.gpt.data.config.chatgpt;

import cn.bugstack.chatglm.session.OpenAiSession;
import cn.bugstack.chatglm.session.OpenAiSessionFactory;
import cn.bugstack.chatglm.session.defaults.DefaultOpenAiSessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description OpenAiSession 工厂配置开启
 * @create 2023-07-16 08:07
 */
@Configuration
@EnableConfigurationProperties(ChatGPTSDKConfigProperties.class)
public class ChatGPTSDKConfig {

//    @Bean
    @ConditionalOnMissingBean
    public OpenAiSession openAiSession(ChatGPTSDKConfigProperties properties) {
        return null;
//        // 1. 配置文件
//        cn.bugstack.chatgpt.session.Configuration configuration = new cn.bugstack.chatgpt.session.Configuration();
//        configuration.setApiHost(properties.getApiHost());
//        configuration.setAuthToken(properties.getAuthToken());
//        configuration.setApiKey(properties.getApiKey());
//
//        // 2. 会话工厂
//        OpenAiSessionFactory factory = new DefaultOpenAiSessionFactory(configuration);
//
//        // 3. 开启会话
//        return factory.openSession();
    }

}
