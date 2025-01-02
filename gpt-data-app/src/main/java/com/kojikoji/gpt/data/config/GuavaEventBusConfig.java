package com.kojikoji.gpt.data.config;

import com.google.common.eventbus.EventBus;
import com.kojikoji.gpt.data.trigger.mq.OrderPaySuccessListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName GuavaEventBusConfig
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/28 21:49
 * @Version
 */
@Configuration
public class GuavaEventBusConfig {

    @Bean
    public EventBus eventBusListener(OrderPaySuccessListener listener) {
        EventBus eventBus = new EventBus();
        eventBus.register(listener);
        return eventBus;
    }
}
