package com.kojikoji.gpt.data.domain.openai.service.rule.impl;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.kojikoji.gpt.data.domain.openai.annotation.LogicStrategy;
import com.kojikoji.gpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.kojikoji.gpt.data.domain.openai.model.entity.PromptEntity;
import com.kojikoji.gpt.data.domain.openai.model.entity.RuleLogicEntity;
import com.kojikoji.gpt.data.domain.openai.model.vo.LogicCheckTypeVO;
import com.kojikoji.gpt.data.domain.openai.service.rule.ILogicFilter;
import com.kojikoji.gpt.data.domain.openai.service.rule.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.print.attribute.standard.Media;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SensitiveWordFilter
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/16 16:40
 * @Version
 */

@Slf4j
@Component
@LogicStrategy(logicModel = DefaultLogicFactory.LogicModel.SENSITIVE_WORD)
public class SensitiveWordFilter implements ILogicFilter {

    @Resource
    private SensitiveWordBs words;

    @Value("${app.config.white-list}")
    private String whiteListStr;

    @Override
    public RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate chatProcess) throws Exception {
        // 白名单用户放行
        if (chatProcess.isWhiteList(whiteListStr)) {
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicCheckTypeVO.SUCCESS)
                    .data(chatProcess)
                    .build();
        }

        ChatProcessAggregate newChatProcessAggregate = new ChatProcessAggregate();
        newChatProcessAggregate.setOpenid(chatProcess.getOpenid());
        newChatProcessAggregate.setModel(chatProcess.getModel());

        List<PromptEntity> newPrompt = chatProcess.getMessages().stream()
                .map(msg -> {
                    String content = msg.getContent();
                    String replace = words.replace(content);
                    return PromptEntity.builder()
                            .role(msg.getRole())
                            .content(replace)
                            .build();
                })
                .collect(Collectors.toList());

        newChatProcessAggregate.setMessages(newPrompt);

        return RuleLogicEntity.<ChatProcessAggregate>builder()
                .type(LogicCheckTypeVO.SUCCESS)
                .data(newChatProcessAggregate)
                .build();
    }
}
