package com.kojikoji.gpt.data.domain.openai.service;

import com.kojikoji.gpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

public interface IChatService {

    ResponseBodyEmitter completions(ChatProcessAggregate chatProcess);
}
