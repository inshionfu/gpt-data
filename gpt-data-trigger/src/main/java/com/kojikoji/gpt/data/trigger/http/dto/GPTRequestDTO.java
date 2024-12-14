package com.kojikoji.gpt.data.trigger.http.dto;

import com.kojikoji.gpt.data.types.enums.GPTModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName ChatGPTRequestDTO
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/12 14:16
 * @Version
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GPTRequestDTO {

    /* 默认模型*/
    private String model = GPTModel.GLM_4_Flash.getCode();

    /* 问题描述*/
    private List<MessageEntity> messages;
}
