package com.kojikoji.gpt.data;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName Application
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/12 16:28
 * @Version
 */

@SpringBootApplication
@Configurable
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
