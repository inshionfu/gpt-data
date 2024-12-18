package com.kojikoji.gpt.data.domain.openai.service.rule;

import com.kojikoji.gpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.kojikoji.gpt.data.domain.openai.model.entity.RuleLogicEntity;

public interface ILogicFilter {

    RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate chatProcess) throws Exception;

}
