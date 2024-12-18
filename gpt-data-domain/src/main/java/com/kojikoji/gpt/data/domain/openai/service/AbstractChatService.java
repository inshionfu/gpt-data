package com.kojikoji.gpt.data.domain.openai.service;

import cn.bugstack.chatglm.session.OpenAiSession;
import com.kojikoji.gpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.kojikoji.gpt.data.domain.openai.model.entity.RuleLogicEntity;
import com.kojikoji.gpt.data.domain.openai.model.vo.LogicCheckTypeVO;
import com.kojikoji.gpt.data.domain.openai.service.rule.factory.DefaultLogicFactory;
import com.kojikoji.gpt.data.types.common.Constants;
import com.kojikoji.gpt.data.types.exception.GPTException;
import javafx.scene.control.RadioMenuItem;
import jdk.nashorn.internal.ir.ReturnNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import javax.xml.bind.annotation.XmlType;

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
        try {
            // 1.请求应答
            emitter.onCompletion(() -> {
                log.info("流式问答请求完成，使用模型：{}", chatProcess.getModel());
            });
            emitter.onError(throwable -> log.error("流式问答请求疫情，使用模型：{}",
                    chatProcess.getModel(), throwable));

            // 2.规则过滤
            RuleLogicEntity<ChatProcessAggregate> ruleLogicEntity = this.doCheckLogic(chatProcess,
                    DefaultLogicFactory.LogicModel.ACCESS_LIMIT.getCode(),
                    DefaultLogicFactory.LogicModel.SENSITIVE_WORD.getCode());

            if (!StringUtils.equals(ruleLogicEntity.getType().getCode(), LogicCheckTypeVO.SUCCESS.getCode())) {
                log.info("规则过滤：{}", ruleLogicEntity.getType().getCode());
                emitter.send(ruleLogicEntity.getInfo());
                emitter.complete();
                return emitter;
            }

            // 3.应答处理
            this.doMessageResponse(chatProcess, emitter);
        } catch (Exception e) {
            throw new GPTException(Constants.ResponseCode.UN_ERROR.getCode(), Constants.ResponseCode.UN_ERROR.getInfo());
        }

        // 3.返回结果
        return emitter;
    }

    protected abstract RuleLogicEntity<ChatProcessAggregate> doCheckLogic(ChatProcessAggregate chatProcess, String... logics) throws Exception;

    protected abstract void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter) throws Exception;
}
