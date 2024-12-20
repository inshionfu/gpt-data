package com.kojikoji.gpt.data.domain.openai.service;

import cn.bugstack.chatglm.model.ChatCompletionRequest;
import cn.bugstack.chatglm.model.ChatCompletionResponse;
import cn.bugstack.chatglm.model.Model;
import cn.bugstack.chatglm.model.Role;
import cn.bugstack.chatglm.session.OpenAiSession;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kojikoji.gpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.kojikoji.gpt.data.types.common.Constants;
import okhttp3.internal.ws.RealWebSocket;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import javax.print.attribute.standard.Media;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ChatService
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/14 20:11
 * @Version
 */

@Service
public class ChatService extends AbstractChatService {

    @Resource
    private OpenAiSession openAiSession;

    @Override
    protected void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter) throws Exception {
        // 1.请求消息
        List<ChatCompletionRequest.Prompt> prompts = chatProcess.getMessages()
                .stream()
                .map(entity -> ChatCompletionRequest.Prompt
                        .builder()
                        .role(Role.valueOf(entity.getRole().toLowerCase()).getCode())
                        .content(entity.getContent())
                        .build())
                .collect(Collectors.toList());

        // 2.封装参数
        ChatCompletionRequest chatRequest = ChatCompletionRequest
                .builder()
                .stream(true)
                .isCompatible(false)
                .prompt(prompts)
                .model(Model.GLM_4_Flash)
                .build();

        // 3.请求应答
        openAiSession.completions(chatRequest, new EventSourceListener() {
            @Override
            public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
                ChatCompletionResponse chatCompletionResponse = JSON.parseObject(data, ChatCompletionResponse.class);
                List<ChatCompletionResponse.Choice> choices = chatCompletionResponse.getChoices();
                for (ChatCompletionResponse.Choice chatChoice : choices) {
                    ChatCompletionResponse.Delta delta = chatChoice.getDelta();
                    if (!Role.assistant.getCode().equals(delta.getRole())) {
                        continue;
                    }

                    // 应答完成
                    String finishReason = chatChoice.getFinishReason();
                    if (StringUtils.isNoneBlank(finishReason) && "stop".equals(finishReason)) {
                        emitter.complete();
                        break;
                    }

                    // 发送信息
                    try {
                        emitter.send(delta.getContent());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}
