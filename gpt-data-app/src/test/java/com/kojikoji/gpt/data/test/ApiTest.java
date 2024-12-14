package com.kojikoji.gpt.data.test;

import cn.bugstack.chatglm.model.*;
import cn.bugstack.chatglm.session.Configuration;
import cn.bugstack.chatglm.session.OpenAiSession;
import cn.bugstack.chatglm.session.OpenAiSessionFactory;
import cn.bugstack.chatglm.session.defaults.DefaultOpenAiSessionFactory;
import cn.bugstack.chatglm.utils.BearerTokenUtils;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kojikoji.gpt.data.trigger.http.dto.MessageEntity;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName ApiTest
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/12 19:19
 * @Version
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Resource
    private OpenAiSession openAiSession;

//    @Before
    public void test_OpenAiSessionFactory() {
        // 1. 配置文件
        Configuration configuration = new Configuration();
        configuration.setApiHost("https://open.bigmodel.cn/api/paas/v4/chat/completions/");
        configuration.setApiSecretKey("e1d64e25bb44d7c570902235ebadfe7e.MU8eKrlWQUk8GXFd");
        configuration.setLevel(HttpLoggingInterceptor.Level.BODY);
        // 2. 会话工厂
        OpenAiSessionFactory factory = new DefaultOpenAiSessionFactory(configuration);
        // 3. 开启会话
        this.openAiSession = factory.openSession();
    }

    @Test
    public void test_gpt_chat_completions_stream() throws JsonProcessingException, InterruptedException {
//        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
//                .builder()
//                .stream(true)
//                .messages(Collections.singletonList(Message.builder().role(Constants.Role.USER).content("写一个java冒泡排序").build()))
//                .model(ChatCompletionRequest.Model.GPT_3_5_TURBO.getCode())
//                .build();
//        EventSource eventSource = openAiSession.chatCompletions(chatCompletionRequest, new EventSourceListener() {
//            @Override
//            public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
//                log.info("测试结果: {}", data);
//            }
//        });
//        new CountDownLatch(1).await();
    }

    @Test
    public void test_glm_chat_completions_stream() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        cn.bugstack.chatglm.model.ChatCompletionRequest req = cn.bugstack.chatglm.model.ChatCompletionRequest.builder()
                .stream(true)
                .isCompatible(false)
                .prompt(new ArrayList<ChatCompletionRequest.Prompt>() {
                    private static final long serialVersionUID = -7988151926241837899L;

                    {
                        add(ChatCompletionRequest.Prompt.builder()
                                .role(Role.user.getCode())
                                .content("写一个java快速排序")
                                .build());
                    }
                })
//                .messages(Collections.singletonList(cn.bugstack.chatglm.model.ChatCompletionRequest.Prompt.builder().content("写一个java冒泡排序").build()))
                .model(Model.GLM_4_Flash)
                .build();
        openAiSession.completions(req, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
                ChatCompletionResponse response = JSON.parseObject(data, ChatCompletionResponse.class);
                log.info("测试结果 onEvent：{}", response.getData());
                // type 消息类型，add 增量，finish 结束，error 错误，interrupted 中断
                if (EventType.finish.getCode().equals(type)) {
                    ChatCompletionResponse.Meta meta = JSON.parseObject(response.getMeta(), ChatCompletionResponse.Meta.class);
                    log.info("[输出结束] Tokens {}", JSON.toJSONString(meta));
                }
            }

            @Override
            public void onClosed(EventSource eventSource) {
                log.info("对话完成");
                latch.countDown();
            }

            @Override
            public void onFailure(EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                log.info("对话异常: {}", response.message(), t);
                latch.countDown();
            }
        });

        // 等待
        latch.await();
    }

    @Test
    public void test_curl() {
        // 1. 配置文件
        Configuration configuration = new Configuration();
        configuration.setApiHost("https://open.bigmodel.cn/");
        configuration.setApiSecretKey("e1d64e25bb44d7c570902235ebadfe7e.MU8eKrlWQUk8GXFd");

        // 2. 获取Token
        String token = BearerTokenUtils.getToken(configuration.getApiKey(), configuration.getApiSecret());
        log.info("1. 在智谱Ai官网，申请 ApiSeretKey 配置到此测试类中，替换 setApiSecretKey 值。 https://open.bigmodel.cn/usercenter/apikeys");
        log.info("2. 运行 test_curl 获取 token：{}", token);
        log.info("3. 将获得的 token 值，复制到 curl.sh 中，填写到 Authorization: Bearer 后面");
        log.info("4. 执行完步骤3以后，可以复制直接运行 curl.sh 文件，或者复制 curl.sh 文件内容到控制台/终端/ApiPost中运行");
    }
}
