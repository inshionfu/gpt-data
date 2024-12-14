package com.kojikoji.gpt.data.trigger.http;

import cn.bugstack.chatglm.model.*;
import cn.bugstack.chatglm.session.OpenAiSession;


import com.alibaba.fastjson.JSON;
import com.kojikoji.gpt.data.trigger.http.dto.GPTRequestDTO;
import com.kojikoji.gpt.data.types.common.Constants;
import com.kojikoji.gpt.data.types.exception.GPTException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @ClassName GPTAIServiceControllerOl
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/12 14:03
 * @Version
 */
@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/v0")
public class GPTAIServiceControllerOld {
    @Resource
    private OpenAiSession openAiSession;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @RequestMapping(value = "chat/completions", method = RequestMethod.POST)
    public ResponseBodyEmitter completionsStream(@RequestBody GPTRequestDTO request,
                                                 @RequestHeader("Authorization") String token,
                                                 HttpServletResponse response) {
        log.info("流式问答请求开始，使用模型：{} 请求信息：{}", request.getModel(),
                JSON.toJSONString(request.getMessages()));
        try {
            // 1.基础配置；流式输出、编码、禁用缓存
            response.setContentType("text/event-stream");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Cache-Control", "no-cache");

            if (!token.equals("b8b6")) {
                throw new RuntimeException("token err");
            }

            // 2.异步处理 HTTP 响应处理类
            ResponseBodyEmitter emitter = new ResponseBodyEmitter(3 * 60 * 1000L);
            emitter.onCompletion(() -> {
                log.info("流式问答请求完成，使用模型：{}", request.getModel());
            });
            emitter.onError(throwable -> log.error("流式问答请求异常，使用模型：{}", request.getModel(), throwable));

            // 3.构建参数
            List<ChatCompletionRequest.Prompt> messages = request.getMessages().stream()
                    .map(entity -> ChatCompletionRequest.Prompt.builder()
                            .role(Role.valueOf(entity.getRole().toLowerCase()).getCode())
                            .content(entity.getContent())
                            .build())
                    .collect(Collectors.toList());

            ChatCompletionRequest chatCompletion = ChatCompletionRequest
                    .builder()
                    .stream(true)
                    .isCompatible(false)
                    .messages(messages)
                    .model(Model.GLM_4_Flash)
                    .build();

            // 4.请求响应
            openAiSession.completions(chatCompletion, new EventSourceListener() {
                @Override
                public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
                    ChatCompletionResponse chatCompletionResponse = JSON.parseObject(data, ChatCompletionResponse.class);
                    List<ChatCompletionResponse.Choice> choices = chatCompletionResponse.getChoices();
                    for (ChatCompletionResponse.Choice choice : choices) {
                        ChatCompletionResponse.Delta delta = choice.getDelta();
                        if (!Role.assistant.getCode().equals(delta.getRole())) {
                            log.info("invalid role: {} content: {}", delta.getRole(), delta.getContent());
                            continue;
                        }

                        // 应答完成
                        String finishReason = choice.getFinishReason();
                        if (StringUtils.isNoneBlank(finishReason) && "stop".equals(finishReason)) {
                            log.info("stream finish {}", finishReason);
                            emitter.complete();
                            break;
                        }

                        // 发送信息
                        try {
                            log.info("send {}", delta.getContent());
                            emitter.send(delta.getContent());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            return emitter;
        } catch (Exception e) {
            log.error("流式应答，请求模型：{} 发生异常", request.getModel(), e);
            throw new GPTException(e.getMessage());
        }
    }

    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    public ResponseBodyEmitter completionsStream(HttpServletResponse response) {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");

        ResponseBodyEmitter emitter = new ResponseBodyEmitter();

        threadPoolExecutor.execute(()->{
            for (int i = 0; i < 10; ++i) {
                try {
                    emitter.send("strddd\r\n" + i);
                    Thread.sleep(100);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            emitter.complete();
        });
        return emitter;
    }
}
