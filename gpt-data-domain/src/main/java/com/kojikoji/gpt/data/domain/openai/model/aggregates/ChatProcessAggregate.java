package com.kojikoji.gpt.data.domain.openai.model.aggregates;

import com.kojikoji.gpt.data.domain.openai.model.entity.PromptEntity;
import com.kojikoji.gpt.data.types.enums.GPTModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName ChatProcessAggregate
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/14 19:59
 * @Version
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatProcessAggregate {
    /* 验证信息*/
    private String token;
    /* 默认模型*/
    private String model = GPTModel.GLM_4_Flash.getCode();
    /* 问题描述*/
    private List<PromptEntity> messages;
}
