package com.kojikoji.gpt.data.domain.openai.service;

import cn.bugstack.chatglm.session.OpenAiSession;
import com.kojikoji.gpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.kojikoji.gpt.data.domain.openai.model.entity.PromptEntity;
import com.kojikoji.gpt.data.types.common.Constants;
import com.kojikoji.gpt.data.types.exception.GPTException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import sun.util.resources.cldr.zh.CalendarData_zh_Hant_TW;

import javax.annotation.Resource;
import javax.xml.ws.RespectBinding;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @ClassName AbstractChatService
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/14 20:04
 * @Version
 */

@Slf4j
public abstract class AbstractChatService implements IChatService {

    @Resource
    protected OpenAiSession openAiSession;

    @Override
    public ResponseBodyEmitter completions(ResponseBodyEmitter emitter, ChatProcessAggregate chatProcess) {
        // 1.请求应答
        emitter.onCompletion(()->{
            log.info("流式问答请求完成，使用模型：{}", chatProcess.getModel());
        });
        emitter.onError(throwable -> log.error("流式问答请求疫情，使用模型：{}",
                chatProcess.getModel(), throwable));

        // 2.应答处理
        try {
            this.doMessageResponse(chatProcess, emitter);
        } catch (Exception e) {
            throw new GPTException(Constants.ResponseCode.UN_ERROR.getCode(), Constants.ResponseCode.UN_ERROR.getInfo());
        }

        // 3.返回结果
        return emitter;
    }

    protected abstract void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter) throws Exception;
}
