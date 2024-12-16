package com.kojikoji.gpt.data.trigger.http;

import com.alibaba.fastjson.JSON;
import com.kojikoji.gpt.data.domain.auth.IAuthService;
import com.kojikoji.gpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.kojikoji.gpt.data.domain.openai.model.entity.PromptEntity;
import com.kojikoji.gpt.data.domain.openai.service.IChatService;
import com.kojikoji.gpt.data.trigger.http.dto.GPTRequestDTO;
import com.kojikoji.gpt.data.trigger.http.dto.MessageEntity;
import com.kojikoji.gpt.data.types.common.Constants;
import com.kojikoji.gpt.data.types.exception.GPTException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * @ClassName GPTAIServiceController
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/12 14:02
 * @Version
 */

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}")
public class GPTAIServiceController {

    @Resource
    private IChatService chatService;

    @Resource
    private IAuthService authService;

    @RequestMapping(value = "chat/completions", method = RequestMethod.POST)
    public ResponseBodyEmitter completionsStream(@RequestBody GPTRequestDTO request, @RequestHeader("Authorization") String token, HttpServletResponse response) {
        log.info("流式问答请求开始，使用模型: {} 请求信息 {}", request.getModel(), JSON.toJSONString(request.getMessages()));
        try {
            // 1.基础配置: 流式输出、编码、禁用缓存
            response.setContentType("text/event-stream");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Cache-Control", "no-cache");

            // 2.构建异步响应对象
            ResponseBodyEmitter emitter = new ResponseBodyEmitter(3 * 60 * 1000L);
            boolean succ = authService.checkToken(token);
            if (!succ) {
                try {
                    emitter.send(Constants.ResponseCode.TOKEN_ERROR.getCode());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                emitter.complete();
                return emitter;
            }

            // 2.参数构建
            ChatProcessAggregate req = ChatProcessAggregate.builder()
                    .model(request.getModel())
                    .token(token)
                    .messages(request.getMessages()
                            .stream()
                            .map(entity-> PromptEntity.builder()
                                    .role(entity.getRole())
                                    .content(entity.getContent())
                                    .build())
                            .collect(Collectors.toList()))
                    .build();

            // 3. 请求结果 & 返回
            return chatService.completions(emitter, req);
        } catch (Exception e) {
            log.error("流式应答，请求模型 {} 发生异常", request.getModel(), e);
            throw new GPTException(e.getMessage());
        }

    }
}
