package com.kojikoji.gpt.data.domain.openai.model.aggregates;

import cn.bugstack.chatglm.session.OpenAiSession;
import com.kojikoji.gpt.data.domain.openai.model.entity.PromptEntity;
import com.kojikoji.gpt.data.types.common.Constants;
import com.kojikoji.gpt.data.types.enums.GPTModel;
import jdk.nashorn.internal.ir.WhileNode;
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
    /* 用户id */
    private String openid;
    /* 默认模型 */
    private String model = GPTModel.GLM_4_Flash.getCode();
    /* 问题描述 */
    private List<PromptEntity> messages;

    public boolean isWhiteList(String whiteListStr) {
        String[] whiteList = whiteListStr.split(Constants.SPLIT);
        for (String whiteOpenid : whiteList) {
            if (whiteOpenid.equals(openid)) {
                return true;
            }
        }
        return false;
    }


}
