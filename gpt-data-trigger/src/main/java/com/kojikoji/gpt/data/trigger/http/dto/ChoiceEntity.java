package com.kojikoji.gpt.data.trigger.http.dto;

import lombok.Data;

/**
 * @ClassName ChoiceEntity
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/12 14:16
 * @Version
 */

@Data
public class ChoiceEntity {
    /* stream = true 请求参数里返回的属性是 delta*/
    private MessageEntity delta;
    /* stream = true 请求参数里返回的属性是 delta*/
    private MessageEntity message;
}
