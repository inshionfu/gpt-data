package com.kojikoji.gpt.data.domain.openai.model.entity;

import lombok.Data;

/**
 * @ClassName ChoiceEntity
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/14 20:02
 * @Version
 */

@Data
public class ChoiceEntity {
    /** stream = true 请求参数里返回的属性是 delta */
    private PromptEntity delta;
    /** stream = false 请求参数里返回的属性是 delta */
    private PromptEntity message;
}
